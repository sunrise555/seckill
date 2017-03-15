package sunny.seckill.service;

import java.util.List;

import sunny.seckill.dto.Exposer;
import sunny.seckill.dto.SeckillExcution;
import sunny.seckill.entity.Seckill;
import sunny.seckill.exception.RepeatKillException;
import sunny.seckill.exception.SeckillCloseException;
import sunny.seckill.exception.SeckillException;

/**
 * ҵ��ӿڣ�վ�ڡ�ʹ���ߡ��ĽǶ����
 * �������棺�����������ȣ��������������ͣ�return ����/�쳣��
 * Created by Sunny on 2017��2��23��
 */
public interface SeckillService {
	/**
	 * function:��ѯ������ɱ��¼
	 * @return
	 */
	//ҳ��չʾ��ɱ�����������Ҫһ����ȡ������ɱ�����ҵ�񷽷�
	List<Seckill> getSeckillList();
	/**
	 * 
	 * function:����ID��ѯ������ɱ����
	 * @param seckillId
	 * @return
	 */
	Seckill getSeckillById(long seckillId);
	/**
	 * ��ɱ����ʱ�����ɱ�ӿڵ�ַ
	 * �������ϵͳʱ�����ɱʱ��
	 * @param seckillId
	 */
	//�û�ͨ���õ�ַ������ɱ
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * ִ����ɱ����
	 * @param seckillId ��ɱID
	 * @param userPhone �û��ֻ���
	 * @param md5
	 * @return
	 */
	SeckillExcution excuteSeckill(long seckillId, long userPhone, String md5) 
	throws SeckillException, RepeatKillException, SeckillCloseException;
	
	/**
	 * ͨ���洢����ִ����ɱ������Ż�
	 * @param seckillId ��ɱID
	 * @param userPhone �û��ֻ���
	 * @param md5
	 * @return ��ɱ���
	 */
	SeckillExcution excuteSeckillByProcedure(long seckillId, long userPhone, String md5);
}
