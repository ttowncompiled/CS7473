package lib.options;

import org.apache.commons.lang3.StringUtils;

public class AckOption {

	public static final String ACK = "ack";
	
	public static AckOption parseOption(String option) {
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new AckOption(Integer.parseInt(option.trim()));
	}
	
	private int ack;
	
	public AckOption(int ack) {
		this.ack = ack;
	}
	
	public boolean checkAck(int ack) {
		return ack == this.ack;
	}
}
