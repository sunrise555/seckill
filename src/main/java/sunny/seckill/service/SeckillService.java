package sunny.seckill.service;

import java.util.List;

import sunny.seckill.dto.Exposer;
import sunny.seckill.dto.SeckillExcution;
import sunny.seckill.entity.Seckill;
import sunny.seckill.exception.RepeatKillException;
import sunny.seckill.exception.SeckillCloseException;
import sunny.seckill.exception.SeckillException;

/**
 * 业务接口：站在“使用者”的角度设计
 * 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 * Created by Sunny on 2017年2月23日
 */
public interface SeckillService {
	/**
	 * function:查询所有秒杀记录
	 * @return
	 */
	//页面展示秒杀结果，所以需要一个获取所有秒杀对象的业务方法
	List<Seckill> getSeckillList();
	/**
	 * 
	 * function:根据ID查询单个秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill getSeckillById(long seckillId);
	/**
	 * 秒杀开启时输出秒杀接口地址
	 * 否则输出系统时间和秒杀时间
	 * @param seckillId
	 */
	//用户通过该地址进入秒杀
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId 秒杀ID
	 * @param userPhone 用户手机号
	 * @param md5
	 * @return
	 */
	SeckillExcution excuteSeckill(long seckillId, long userPhone, String md5) 
	throws SeckillException, RepeatKillException, SeckillCloseException;
	
	/**
	 * 通过存储过程执行秒杀，深度优化
	 * @param seckillId 秒杀ID
	 * @param userPhone 用户手机号
	 * @param md5
	 * @return 秒杀结果
	 */
	SeckillExcution excuteSeckillByProcedure(long seckillId, long userPhone, String md5);
}
