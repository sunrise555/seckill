package sunny.seckill.enums;
/**
 * ʹ��ö�ٱ������������ֶ�
 * Created by Sunny on 2017��2��24��
 */
public enum SeckillStatEnum {

	//��װstate��stateInfo�ֶ�
	SUCCESS(1, "��ɱ�ɹ�"),
	END(0, "��ɱ����"),
	REPEAT_KILL(-1, "�ظ���ɱ"),
	INNER_ERROR(-2, "ϵͳ�쳣"),
	DATA_REWRITE(-3, "���ݴ۸�");
	
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
	
	//ö��
	//ͨ��ID�����ض�Ӧ��״̬�ַ���
	public static SeckillStatEnum stateOf(int index){
		for (SeckillStatEnum state : values()) {
			if (state.getState() == index)
				return state;
		}
		return null;
	}
	
}
