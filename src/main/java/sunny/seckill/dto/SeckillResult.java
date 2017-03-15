package sunny.seckill.dto;
/**
 * ����ajax���󷵻ص����ͣ���װjson���
 * Created by Sunny on 2017��3��1��
 */
//DTO����װ������Ҫ���ݵ�����
public class SeckillResult<T> {
	//�����Ƿ����
	private boolean success;
	
	private T data;
	//���ݲ����õĶ�������Ϣ
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
