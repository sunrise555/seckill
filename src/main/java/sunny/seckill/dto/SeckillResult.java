package sunny.seckill.dto;
/**
 * 所有ajax请求返回的类型，封装json结果
 * Created by Sunny on 2017年3月1日
 */
//DTO：封装所有需要传递的数据
public class SeckillResult<T> {
	//数据是否可用
	private boolean success;
	
	private T data;
	//数据不可用的而错误信息
	private String errorInfo;

	public SeckillResult(){
		
	}
	
	public SeckillResult(boolean success, T data, String errorInfo) {
		this.success = success;
		this.data = data;
		this.errorInfo = errorInfo;
	}

	public SeckillResult(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public SeckillResult(boolean success, String errorInfo) {
		this.success = success;
		this.errorInfo = errorInfo;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the errorInfo
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * @param errorInfo the errorInfo to set
	 */
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	
}
