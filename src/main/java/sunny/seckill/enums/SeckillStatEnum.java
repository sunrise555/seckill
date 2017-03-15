package sunny.seckill.enums;
/**
 * 使用枚举表述常量数据字段
 * Created by Sunny on 2017年2月24日
 */
public enum SeckillStatEnum {

	//封装state、stateInfo字段
	SUCCESS(1, "秒杀成功"),
	END(0, "秒杀结束"),
	REPEAT_KILL(-1, "重复秒杀"),
	INNER_ERROR(-2, "系统异常"),
	DATA_REWRITE(-3, "数据篡改");
	
	private int state;
	
	private String stateInfo;

	SeckillStatEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
	
	//枚举
	//通过ID，返回对应的状态字符串
	public static SeckillStatEnum stateOf(int index){
		for (SeckillStatEnum state : values()) {
			if (state.getState() == index)
				return state;
		}
		return null;
	}
	
}
