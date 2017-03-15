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
 * 配置spring和junit整合 RunWith(SpringJunit4ClassRunner)
 * junit启动时加载spring IOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入Dao的实现类依赖
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
    	/*Java本身的语言特性是不识别形参，例如运行时Java是把queryAll(offset, limit)看作是queryAll(args0, args1)
    	 * 所以在xml中limit #{offset},#{limit}传递多个参数数需要指明对应关系
    	 * 使用注解@Param{"形参名称"}来修改接口SeckillDao.java
    	 * List<Seckill> queryAll(@Param("offset") int offset, @Param("offset") int limit);
    	 * */
    	List<Seckill> seckills = seckillDao.queryAll(0, 100);
    	for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
    }

}