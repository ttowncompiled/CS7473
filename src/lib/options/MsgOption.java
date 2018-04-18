package lib.options;

public class MsgOption {

	public static final String MSG = "msg";
	
	public static MsgOption parseOption(String option) {
		if (option.trim().equals("")) {
			return null;
		}
		return new MsgOption(option.trim());
	}
	
	private String msg;
	
	public MsgOption(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
}
