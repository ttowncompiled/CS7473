package lib.rules;

import org.apache.commons.lang3.StringUtils;

import util.Utils;

public class IPMaskRule {

	public static IPMaskRule parseIPMask(String ipMask) {
		if (ipMask.equals(Rule.ANY)) {
			return new IPMaskRule();
		}
		String[] parts = ipMask.split("/");
		if (! StringUtils.isNumeric(parts[1])) {
			return null;
		}
		return new IPMaskRule(Utils.convertIPAddress(parts[0]), Integer.parseInt(parts[1]));
	}
	
	private int ip;
	private int mask;
	private boolean any;
	
	public IPMaskRule(int ip, int mask) {
		this.ip = ip;
		this.mask = mask;
	}
	
	public IPMaskRule() {
		this.any = true;
	}
	
	public int getIP() {
		return this.ip;
	}
	
	public int getMask() {
		return this.mask;
	}
	
	public boolean isAny() {
		return this.any;
	}
}
