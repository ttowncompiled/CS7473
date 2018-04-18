package lib.options;

import org.apache.commons.lang3.StringUtils;

public class SIDOption {

	public static final String SID = "sid";
	
	public static SIDOption parseOption(String option) {
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new SIDOption(Integer.parseInt(option.trim()));
	}
	
	private int sid;
	
	public SIDOption(int sid) {
		this.sid = sid;
	}
	
	public int getSID() {
		return this.sid;
	}
}
