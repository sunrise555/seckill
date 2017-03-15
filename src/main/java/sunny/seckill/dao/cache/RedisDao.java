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
 * Created by Sunny on 2017年3月9日
 */
public class RedisDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//数据库连接池
	private JedisPool jedisPool;
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	
	public Seckill getSeckill(long seckillId) {
		//redis操作：缓存
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				//jedis内部没有实现序列化
				// get->byte[] -> 反序列化 ->Object(Seckill)
				//采用自定义序列化方式
				byte[] bytes = jedis.get(key.getBytes());
				//缓存重新取到
				if(null != bytes) {
					Seckill seckill = schema.newMessage();//获取一个seckill类型的空对象
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);//seckill就被反序列化
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
		// 先将seckil序列化->bytes[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// 缓存时间
				int timeout = 60 * 60;//秒为单位，缓存时间1小时
				//超时缓存
				String result = jedis.setex(key.getBytes(), timeout, bytes);//成功后会返回OK
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
