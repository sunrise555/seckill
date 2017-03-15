package sunny.seckill.dao;

import org.apache.ibatis.annotations.Param;

import sunny.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	/**
	 * 插入购买明细，可通过联合组键过滤重复
	 * @author Administrator
	 * @create_time 2017年2月18日 下午3:36:38
	 * @modify_time 2017年2月18日 下午3:36:38 
	 * @param seckillId
	 * @param usePhone
	 * @return 插入的记录的条数，返回0代表插入失败
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
	
	/**
	 * 根据ID查询秒杀成功对象，并携带秒杀产品实体对象
	 * @author Administrator
	 * @create_time 2017年2月18日 下午3:37:59
	 * @modify_time 2017年2月18日 下午3:37:59 
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
