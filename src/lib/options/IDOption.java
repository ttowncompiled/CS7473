package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.IPHeader;
import lib.packets.Packet;
import util.Config;

public class IDOption {

	public static final String IPID = "id";
	
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
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			IPHeader header = (IPHeader) p.getHeader();
			return this.checkID(Short.toUnsignedInt(header.getIdentification()));
		}
		return false;
	}
}
