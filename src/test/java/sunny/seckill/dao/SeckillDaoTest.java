package sunny.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sunny.seckill.entity.Seckill;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2017/2/22.
 * ����spring��junit���� RunWith(SpringJunit4ClassRunner)
 * junit����ʱ����spring IOC����
 */
@RunWith(SpringJUnit4ClassRunner.class)
//����junit spring�������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //ע��Dao��ʵ��������
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception {
    	Date killTime = new Date();
    	int updateCount = seckillDao.reduceNumber(1000L, killTime);
    	System.out.println(updateCount);
    }

    @Test
    public void testQueryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());

    }

    @Test
    public void testQueryAll() throws Exception {
    	//org.mybatis.spring.MyBatisSystemException: 
    	//nested exception is org.apache.ibatis.binding.BindingException: 
    	//Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
    	/*Java��������������ǲ�ʶ���βΣ���������ʱJava�ǰ�queryAll(offset, limit)������queryAll(args0, args1)
    	 * ������xml��limit #{offset},#{limit}���ݶ����������Ҫָ����Ӧ��ϵ
    	 * ʹ��ע��@Param{"�β�����"}���޸Ľӿ�SeckillDao.java
    	 * List<Seckill> queryAll(@Param("offset") int offset, @Param("offset") int limit);
    	 * */
    	List<Seckill> seckills = seckillDao.queryAll(0, 100);
    	for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
    }

}