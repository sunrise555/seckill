package sunny.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sunny.seckill.dto.Exposer;
import sunny.seckill.dto.SeckillExcution;
import sunny.seckill.entity.Seckill;
import sunny.seckill.exception.RepeatKillException;
import sunny.seckill.exception.SeckillCloseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml", 
	"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

	//获取日志对象
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> seckillList =  seckillService.getSeckillList();
		logger.info("seckillList={}", seckillList);
		//Closing non transactional SqlSession非事务
	}

	@Test
	public void testGetSeckillById() throws Exception {
		long seckillId = 1000L;
		Seckill seckill = seckillService.getSeckillById(seckillId);
		logger.info("seckill={}", seckill);
		//Closing non transactional SqlSession
	}

	@Test
	public void testExportSeckillUrl() throws Exception {
		long seckillId = 1000L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
//		Date date = new Date(exposer.getNow());
//		System.out.println(date);
		logger.info("exposer={}", exposer);
		//exposed=true, md5=1bd99fa2cf002e39d9674725322f3edf, seckillId=1000, now=0, start=0, end=0]
	}

	@Test
	public void testExcuteSeckill() throws Exception {
		long seckillId = 1000L;
		long phone = 1234567890123L;
		String md5 = "1bd99fa2cf002e39d9674725322f3edf";
		try {
			SeckillExcution  seckillExcution  = seckillService.excuteSeckill(seckillId, phone, md5);
			logger.info("seckillExcution={}", seckillExcution);
		} catch (SeckillCloseException e) {
			logger.error(e.getMessage());
		} catch (RepeatKillException e) {
			logger.error(e.getMessage());
		}		
	}
	 @Test
	 public void testExcuteSeckillByProcedure() throws Exception {
		 long seckillId = 1000L;
		 long phone = 1234567890123L;
		 Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		 if(exposer.isExposed()) {
			 String md5 = exposer.getMd5();
			 SeckillExcution  seckillExcution = seckillService.excuteSeckillByProcedure(seckillId, phone, md5);
			 logger.info(seckillExcution.getStateInfo());
		 }
		
	 }
}
