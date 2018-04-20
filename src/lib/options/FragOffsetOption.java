package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.IPHeader;
import lib.packets.Packet;
import util.Config;

public class FragOffsetOption {
	
	public static final String FRAGOFF = "fragoffset";
	
	private static final int NEG = 0;
	private static final int LT = 1;
	private static final int GT = 2;
	
	public static FragOffsetOption parseOption(String option) {
		if (option.charAt(0) == '!') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new FragOffsetOption(Integer.parseInt(option.substring(1).trim()), FragOffsetOption.NEG);
		} else if (option.charAt(0) == '<') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new FragOffsetOption(Integer.parseInt(option.substring(1).trim()), FragOffsetOption.LT);
		} else if (option.charAt(0) == '>') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new FragOffsetOption(Integer.parseInt(option.substring(1).trim()), FragOffsetOption.GT);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new FragOffsetOption(Integer.parseInt(option.trim()));
	}
	
	private int offset;
	private int code;
	
	public FragOffsetOption(int offset) {
		this.offset = offset;
		this.code = -1;
	}
	
	public FragOffsetOption(int offset, int code) {
		this.offset = offset;
		this.code = code;
	}
	
	public boolean checkFragOffset(int offset) {
		switch (this.code) {
			case FragOffsetOption.NEG:
				return offset != this.offset;
			case FragOffsetOption.LT:
				return offset < this.offset;
			case FragOffsetOption.GT:
				return offset > this.offset;
			default:
				return offset == this.offset;
		}
	}
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			IPHeader header = (IPHeader) p.getHeader();
			return this.checkFragOffset(Short.toUnsignedInt(header.getFragmentationOffset()));
		}
		return false;
	}
}
