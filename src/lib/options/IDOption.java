package lib.options;

import org.apache.commons.lang3.StringUtils;

public class IDOption {

	public static final String IP_ID = "id";
	
	public static IDOption parseOption(String option) {
		if (! StringUtils.isNumeric(option)) {
			return null;
		}
		return new IDOption(Integer.parseInt(option));
	}
	
	private int ipid;
	
	public IDOption(int ipid) {
		this.ipid = ipid;
	}
	
	public boolean checkID(int ipid) {
		return ipid == this.ipid;
	}
}
