package sunny.seckill.entity;

import java.util.Date;

/**
 * ��ɱ�ɹ���¼��ʵ����
 * @author sunny
 * @create_time 2017��2��18�� ����3:21:32
 * @modify_time 2017��2��18�� ����3:21:32
 */
public class SuccessKilled {
	
	private long seckillId;
	
	private long usePhone;
	
	private int state;
		
	private Date createTime;
	
	//������ɱ�ɹ��ļ�¼������ϣ��Ҳ����ȡ����Ӧ����ɱ����Seckill
	//��ζ�Ŷ��һ�Ĺ�ϵ
	private Seckill seckill;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUsePhone() {
		return usePhone;
	}

	public void setUsePhone(long usePhone) {
		this.usePhone = usePhone;
	}

	public int isState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

	/**
	 * @return the seckill
	 */
	public Seckill getSeckill() {
		return seckill;
	}

	/**
	 * @param seckill the seckill to set
	 */
	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", usePhone="
				+ usePhone + ", state=" + state + ", createTime=" + createTime
				+ "]";
	}

	
	
	
}
