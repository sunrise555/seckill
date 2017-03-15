package sunny.seckill.exception;
/**
 * 重复秒杀异常（运行期异常）
 * Created by Sunny on 2017年2月23日
 */
public class RepeatKillException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RepeatKillException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
