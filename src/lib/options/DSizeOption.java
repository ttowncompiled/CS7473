package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.packets.Packet;

public class DSizeOption {

	public static final String DSIZE = "dsize";
	
	private static final int LT = 1;
	private static final int GT = 2;
	private static final int DIFF = 5;
	
	public static DSizeOption parseOption(String option) {
		if (option.charAt(0) == '<') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new DSizeOption(Integer.parseInt(option.substring(1).trim()), DSizeOption.LT);
		} else if (option.charAt(0) == '>') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new DSizeOption(Integer.parseInt(option.substring(1).trim()), DSizeOption.GT);
		}
		if (option.contains("<>")) {
			String[] parts = option.trim().split("<>");
			if (parts[0] == "" || parts[1] == "") {
				return null;
			}
			if (! StringUtils.isNumeric(parts[0]) || ! StringUtils.isNumeric(parts[1])) {
				return null;
			}
			return new DSizeOption(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), DSizeOption.DIFF);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new DSizeOption(Integer.parseInt(option.trim()));
	}
	
	private int dsize;
	private int dsize2;
	private int code;
	
	public DSizeOption(int dsize) {
		this.dsize = dsize;
		this.dsize2 = -1;
		this.code = -1;
	}
	
	public DSizeOption(int dsize, int code) {
		this.dsize = dsize;
		this.dsize2 = -1;
		this.code = code;
	}
	
	public DSizeOption(int dsize, int dsize2, int code) {
		this.dsize = dsize;
		this.dsize2 = dsize2;
		this.code = code;
	}
	
	public boolean checkDSize(int dsize) {
		switch (this.code) {
			case DSizeOption.LT:
				return dsize < this.dsize;
			case DSizeOption.GT:
				return dsize > this.dsize;
			case DSizeOption.DIFF:
				return this.dsize <= dsize && dsize <= this.dsize2;
			default:
				return dsize == this.dsize;
		}
	}
	
	public boolean checkPacket(Packet p) {
		return this.checkDSize(p.size());
	}
}
