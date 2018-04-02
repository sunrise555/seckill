package sunny.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import sunny.seckill.dao.SeckillDao;
import sunny.seckill.dao.SuccessKilledDao;
import sunny.seckill.dao.cache.RedisDao;
import sunny.seckill.dto.Exposer;
import sunny.seckill.dto.SeckillExcution;
import sunny.seckill.entity.Seckill;
import sunny.seckill.entity.SuccessKilled;
import sunny.seckill.enums.SeckillStatEnum;
import sunny.seckill.exception.RepeatKillException;
import sunny.seckill.exception.SeckillCloseException;
import sunny.seckill.exception.SeckillException;
import sunny.seckill.service.SeckillService;

@Service
public class SeckillServiceImpl implements SeckillService {
	// 获取日志对象
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入service
	@Autowired
	// @Resource
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	// 引入混淆概念，越复杂越好,用于混淆MD5
	private final String slat = "dhiash832*^*^&gxsaqje2$#$";

	// 注入redis
	@Autowired
	private RedisDao redisDao;

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getSeckillById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	/* 使用redis优化 */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		/*
		 * 优化查询：缓存优化
		 * 
		 * get from cache if null get db else put cache return ...
		 */

		// 1、访问redis
		Seckill seckill = redisDao.getSeckill(seckillId);

		if (null == seckill) {
			// 2、访问数据库
			seckill = seckillDao.queryById(seckillId);
			if (null == seckill) {
				return new Exposer(false, seckillId);
			} else {
				// 3、放入redis
				redisDao.putSeckill(seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// 获取系统当前时间
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}
		// 生成MD5，转化特定字符串的过程，最大特点是不可逆，加入混淆的概念slat
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	@Transactional
	/*
	 * 将异常转为运行期异常，这样才能回滚 使用注解控制事务方法的优点:
	 * 1、开发团队达成一致，明确标注事务方法的编程风格
	 * 2、保证事务方法的执行时间尽可能的短
	 * 3、不穿插其他的网络操作：RPC、HTTP，因为这些请求耗时秒级，对高并发有影响，如果一定要用将其剥离到事务方法外部
	 * 4、并不是所有的方法都需要事务，比如只有一天修改操作，或者只读操作不需要事务操作，具体参见mysql行级锁
	 */
	public SeckillExcution excuteSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		if (null == md5 || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀逻辑：减库存+记录购买行为
		Date nowTime = new Date();

		try {
			// int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			// if (updateCount <= 0) {
			// throw new SeckillCloseException("seckill is closed");
			// } else {
			// int insertCount = successKilledDao.insertSuccessKilled(seckillId,
			// userPhone);
			// //唯一验证：同一个用户一个电话号码只能秒杀一次
			// if (insertCount <= 0) {
			// throw new RepeatKillException("seckill repeated");
			// }else {
			// //秒杀成功
			// SuccessKilled successKilled =
			// successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
			// return new SeckillExcution(seckillId, SeckillStatEnum.SUCCESS,
			// successKilled);
			// }
			// }
			/* 简单优化，通过先执行不需要行级锁的操作insert减少网络延迟 */
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);
			// 唯一验证：同一个用户一个电话号码只能秒杀一次
			if (insertCount <= 0) {
				throw new RepeatKillException("seckill repeated");
			} else {
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					throw new SeckillCloseException("seckill is closed");
				} else {
					// 秒杀成功
					SuccessKilled successKilled = successKilledDao
							.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExcution(seckillId,
							SeckillStatEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常转化为运行期异常，一旦发生错误，就要回滚
			throw new SeckillException("seckill inner error: " + e.getMessage());
		}
	}

	@Override
	public SeckillExcution excuteSeckillByProcedure(long seckillId,
			long userPhone, String md5) {
		if (null == md5 || !md5.equals(getMD5(seckillId))) {
			return new SeckillExcution(seckillId, SeckillStatEnum.DATA_REWRITE);
		}
		
		Date nowTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", nowTime);
		map.put("result", null);
		//通过map，可以在逻辑执行完后，通过map.get(key)获取result的值
		try {
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result",-2);
			if (result == 1) {
				SuccessKilled successKilled = successKilledDao
						.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExcution(seckillId,
						SeckillStatEnum.SUCCESS, successKilled);
			}else {
				return new SeckillExcution(seckillId,
						SeckillStatEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillExcution(seckillId,
					SeckillStatEnum.INNER_ERROR);
		}
	}
}
