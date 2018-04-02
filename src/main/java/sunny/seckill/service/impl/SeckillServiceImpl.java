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
	// ��ȡ��־����
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// ע��service
	@Autowired
	// @Resource
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	// ����������Խ����Խ��,���ڻ���MD5
	private final String slat = "dhiash832*^*^&gxsaqje2$#$";

	// ע��redis
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

	/* ʹ��redis�Ż� */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		/*
		 * �Ż���ѯ�������Ż�
		 * 
		 * get from cache if null get db else put cache return ...
		 */

		// 1������redis
		Seckill seckill = redisDao.getSeckill(seckillId);

		if (null == seckill) {
			// 2���������ݿ�
			seckill = seckillDao.queryById(seckillId);
			if (null == seckill) {
				return new Exposer(false, seckillId);
			} else {
				// 3������redis
				redisDao.putSeckill(seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// ��ȡϵͳ��ǰʱ��
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}
		// ����MD5��ת���ض��ַ����Ĺ��̣�����ص��ǲ����棬��������ĸ���slat
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
	 * ���쳣תΪ�������쳣���������ܻع� ʹ��ע��������񷽷����ŵ�:
	 * 1�������ŶӴ��һ�£���ȷ��ע���񷽷��ı�̷��
	 * 2����֤���񷽷���ִ��ʱ�価���ܵĶ�
	 * 3�����������������������RPC��HTTP����Ϊ��Щ�����ʱ�뼶���Ը߲�����Ӱ�죬���һ��Ҫ�ý�����뵽���񷽷��ⲿ
	 * 4�����������еķ�������Ҫ���񣬱���ֻ��һ���޸Ĳ���������ֻ����������Ҫ�������������μ�mysql�м���
	 */
	public SeckillExcution excuteSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		if (null == md5 || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// ִ����ɱ�߼��������+��¼������Ϊ
		Date nowTime = new Date();

		try {
			// int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			// if (updateCount <= 0) {
			// throw new SeckillCloseException("seckill is closed");
			// } else {
			// int insertCount = successKilledDao.insertSuccessKilled(seckillId,
			// userPhone);
			// //Ψһ��֤��ͬһ���û�һ���绰����ֻ����ɱһ��
			// if (insertCount <= 0) {
			// throw new RepeatKillException("seckill repeated");
			// }else {
			// //��ɱ�ɹ�
			// SuccessKilled successKilled =
			// successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
			// return new SeckillExcution(seckillId, SeckillStatEnum.SUCCESS,
			// successKilled);
			// }
			// }
			/* ���Ż���ͨ����ִ�в���Ҫ�м����Ĳ���insert���������ӳ� */
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);
			// Ψһ��֤��ͬһ���û�һ���绰����ֻ����ɱһ��
			if (insertCount <= 0) {
				throw new RepeatKillException("seckill repeated");
			} else {
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��ɱ�ɹ�
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
			// ���б������쳣ת��Ϊ�������쳣��һ���������󣬾�Ҫ�ع�
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
		//ͨ��map���������߼�ִ�����ͨ��map.get(key)��ȡresult��ֵ
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
