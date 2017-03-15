package sunny.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sunny.seckill.entity.Seckill;

/**
 * 
 * Created by Sunny on 2017��3��9��
 */
public class RedisDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//���ݿ����ӳ�
	private JedisPool jedisPool;
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	
	public Seckill getSeckill(long seckillId) {
		//redis����������
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				//jedis�ڲ�û��ʵ�����л�
				// get->byte[] -> �����л� ->Object(Seckill)
				//�����Զ������л���ʽ
				byte[] bytes = jedis.get(key.getBytes());
				//��������ȡ��
				if(null != bytes) {
					Seckill seckill = schema.newMessage();//��ȡһ��seckill���͵Ŀն���
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);//seckill�ͱ������л�
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String putSeckill(Seckill seckill) {
		// �Ƚ�seckil���л�->bytes[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// ����ʱ��
				int timeout = 60 * 60;//��Ϊ��λ������ʱ��1Сʱ
				//��ʱ����
				String result = jedis.setex(key.getBytes(), timeout, bytes);//�ɹ���᷵��OK
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
