package lib.rules;

import org.apache.commons.lang3.StringUtils;

import lib.Showable;
import lib.packets.IPPacket;
import lib.packets.Packet;
import util.BitString;
import util.Config;
import util.Utils;

public class IPMaskRule implements Showable {

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
	
	public boolean checkIP(int ip) {
		if (this.isAny()) {
			return true;
		}
		if (this.mask <= 0) {
			return this.ip == ip;
		}
		return BitString.fromInt(this.ip).substring(this.mask).equals(BitString.fromInt(ip).substring(this.mask)); 
	}
	
	@Override
	public String toString() {
		if (this.isAny()) {
			return Rule.ANY;
		}
		return new StringBuilder().append(Utils.convertToIPAddress(this.ip)).append("/").append(this.mask).toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
