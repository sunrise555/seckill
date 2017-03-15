package sunny.seckill.exception;
/**
 * 秒杀关闭异常
 * Created by Sunny on 2017年2月23日
 */
public class SeckillCloseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SeckillCloseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
