package sunny.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import sunny.seckill.entity.Seckill;
/**
 * 
 * Created by Sunny on 2017��3��6��
 */
public interface SeckillDao {
		/**
		 * �����
		 * @author Administrator
		 * @create_time 2017��2��18�� ����3:31:59
		 * @modify_time 2017��2��18�� ����3:31:59 
		 * @param seckillId 
		 * @param killTime
		 * @return ������ֵ>1ʱ��ָ���¼�¼��������=0˵��û��ִ�и���
		 */
		int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
		/**
		 * ����ID��ѯ
		 * @author Administrator
		 * @create_time 2017��2��18�� ����3:33:16
		 * @modify_time 2017��2��18�� ����3:33:16 
		 * @param seckillId
		 * @return
		 */
		Seckill queryById(long seckillId);
		/**
		 * ����ƫ������ѯ��ɱ�����б�
		 * @author Administrator
		 * @create_time 2017��2��18�� ����3:34:19
		 * @modify_time 2017��2��18�� ����3:34:19 
		 * @param offset
		 * @param limit
		 * @return
		 */
		List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
		
		/**
		 * ʹ�ô洢����ִ����ɱ
		 * @param paraMap
		 */
		void killByProcedure(Map<String, Object> paraMap);
}
