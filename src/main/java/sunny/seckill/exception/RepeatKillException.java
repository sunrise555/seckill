package sunny.seckill.exception;
/**
 * �ظ���ɱ�쳣���������쳣��
 * Created by Sunny on 2017��2��23��
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
