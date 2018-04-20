package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.IPHeader;
import lib.packets.Packet;
import util.Config;

public class TOSOption {

	public static final String TOS = "tos";
	
	private static final int NEG = 0;
	
	public static TOSOption parseOption(String option) {
		if (option.charAt(0) == '!') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new TOSOption(Integer.parseInt(option.substring(1).trim()), TOSOption.NEG);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new TOSOption(Integer.parseInt(option.trim()));
	}
	
	private int tos;
	private int code;
	
	public TOSOption(int tos) {
		this.tos = tos;
		this.code = -1;
	}
	
	public TOSOption(int tos, int code) {
		this.tos = tos;
		this.code = code;
	}
	
	public boolean checkTOS(int tos) {
		switch (this.code) {
			case TOSOption.NEG:
				return tos != this.tos;
			default:
				return tos == this.tos;
		}
	}
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			IPHeader header = (IPHeader) p.getHeader();
			return this.checkTOS(Byte.toUnsignedInt(header.getTypeOfService()));
		}
		return false;
	}
}
