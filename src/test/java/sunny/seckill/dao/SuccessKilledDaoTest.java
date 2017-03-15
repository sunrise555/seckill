package sunny.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sunny.seckill.entity.SuccessKilled;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	@Resource
	private SuccessKilledDao successKilledDao;
	@Test
	public void testInsertSuccessKilled() {
		long id = 1000L;
		long phone = 18202795887L;
		int updateCount = successKilledDao.insertSuccessKilled(id, phone);
		System.out.println("updateCount=" + updateCount);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		long id = 1000L;
		long phone = 18202795887L;
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
		//System.out.println(successKilled.getSeckill().getName());
		System.out.println(successKilled);
	}

}
