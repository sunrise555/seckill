package sunny.seckill.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sunny.seckill.dto.Exposer;
import sunny.seckill.dto.SeckillExcution;
import sunny.seckill.dto.SeckillResult;
import sunny.seckill.entity.Seckill;
import sunny.seckill.enums.SeckillStatEnum;
import sunny.seckill.exception.RepeatKillException;
import sunny.seckill.exception.SeckillCloseException;
import sunny.seckill.service.SeckillService;


/**
 * 
 * Created by Sunny on 2017年3月1日
 */

@Controller
/* 注入service */
@RequestMapping("/seckill")
/* 访问的URL：/模块/资源/{ID}/细分 seckill/list */
public class SeckillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		/*
		 * list.jsp + model = ModelAndView
		 */
		return "list";// /WEB-INF/jsp/list.jsp
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (null == seckillId) {
			System.out.println("redirect:seckillId==null");
			return "redirect:/seckill/list";
			
		}
		Seckill seckill = seckillService.getSeckillById(seckillId);
		if (null == seckill) {
			System.out.println("forward:seckill is null");
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	// ajax json
	@RequestMapping(value = "/{seckillId}/exposer", 
			method = RequestMethod.POST, 
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody // 该注解可以告诉spring将数据封装为json
	public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExcution> execute(
			@PathVariable("seckillId") Long seckillId,
			@CookieValue(value = "killPhone", required = false) Long userPhone,
			@PathVariable("md5") String md5) {

		if (null == userPhone) {
			return new SeckillResult<SeckillExcution>(false, "用户未注册");
		}
		try {
			SeckillExcution execution = seckillService.excuteSeckill(seckillId,
					userPhone, md5);
			return new SeckillResult<SeckillExcution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExcution execution = new SeckillExcution(seckillId,
					SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExcution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExcution execution = new SeckillExcution(seckillId,
					SeckillStatEnum.END);
			return new SeckillResult<SeckillExcution>(true, execution);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			SeckillExcution execution = new SeckillExcution(seckillId,
					SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExcution>(true, execution);
		}
	}
	
	/*
	 * 获取系统时间
	 */
	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	@ResponseBody
	 public SeckillResult<Long> time() {
		Date now = new Date();
		 return new SeckillResult<Long>(true, now.getTime());
	 }
}

