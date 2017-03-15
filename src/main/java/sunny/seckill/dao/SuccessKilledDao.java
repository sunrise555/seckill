package sunny.seckill.dao;

import org.apache.ibatis.annotations.Param;

import sunny.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	/**
	 * ���빺����ϸ����ͨ��������������ظ�
	 * @author Administrator
	 * @create_time 2017��2��18�� ����3:36:38
	 * @modify_time 2017��2��18�� ����3:36:38 
	 * @param seckillId
	 * @param usePhone
	 * @return ����ļ�¼������������0�������ʧ��
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
	
	/**
	 * ����ID��ѯ��ɱ�ɹ����󣬲�Я����ɱ��Ʒʵ�����
	 * @author Administrator
	 * @create_time 2017��2��18�� ����3:37:59
	 * @modify_time 2017��2��18�� ����3:37:59 
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
