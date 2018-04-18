package lib.options;

public class LogToOption {

	public static final String LOGTO = "logto";
	
	public static LogToOption parseOption(String option) {
		if (option.trim().equals("")) {
			return null;
		}
		return new LogToOption(option.trim());
	}
	
	private String logto;
	
	public LogToOption(String logto) {
		this.logto = logto;
	}
	
	public String getLogTo() {
		return this.logto;
	}
}
