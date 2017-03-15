package sunny.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import sunny.seckill.entity.Seckill;
/**
 * 
 * Created by Sunny on 2017年3月6日
 */
public interface SeckillDao {
		/**
		 * 减库存
		 * @author Administrator
		 * @create_time 2017年2月18日 下午3:31:59
		 * @modify_time 2017年2月18日 下午3:31:59 
		 * @param seckillId 
		 * @param killTime
		 * @return 当返回值>1时，指更新记录的条数，=0说明没有执行更新
		 */
		int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
		/**
		 * 根据ID查询
		 * @author Administrator
		 * @create_time 2017年2月18日 下午3:33:16
		 * @modify_time 2017年2月18日 下午3:33:16 
		 * @param seckillId
		 * @return
		 */
		Seckill queryById(long seckillId);
		/**
		 * 根据偏移量查询秒杀对象列表
		 * @author Administrator
		 * @create_time 2017年2月18日 下午3:34:19
		 * @modify_time 2017年2月18日 下午3:34:19 
		 * @param offset
		 * @param limit
		 * @return
		 */
		List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
		
		/**
		 * 使用存储过程执行秒杀
		 * @param paraMap
		 */
		void killByProcedure(Map<String, Object> paraMap);
}
