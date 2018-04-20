package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.IPHeader;
import lib.packets.Packet;
import util.Config;

public class TTLOption {
	
	public static final String TTL = "ttl";
	
	private static final int LT = 1;
	private static final int GT = 2;
	private static final int LTE = 3;
	private static final int GTE = 4;
	private static final int DIFF = 5;
	
	public static TTLOption parseOption(String option) {
		if (option.charAt(0) == '<') {
			if (option.charAt(1) == '=') {
				if (! StringUtils.isNumeric(option.substring(2).trim())) {
					return null;
				}
				return new TTLOption(Integer.parseInt(option.substring(2).trim()), TTLOption.LTE);
			}
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new TTLOption(Integer.parseInt(option.substring(1).trim()), TTLOption.LT);
		} else if (option.charAt(0) == '>') {
			if (option.charAt(1) == '=') {
				if (! StringUtils.isNumeric(option.substring(2).trim())) {
					return null;
				}
				return new TTLOption(Integer.parseInt(option.substring(2).trim()), TTLOption.GTE);
			}
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new TTLOption(Integer.parseInt(option.substring(1).trim()), TTLOption.GT);
		} else if (option.charAt(0) == '=') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new TTLOption(Integer.parseInt(option.substring(1).trim()));
		}
		if (option.contains("-")) {
			String[] parts = option.trim().split("-");
			if (parts[0].equals("")) {
				if (! StringUtils.isNumeric(parts[1])) {
					return null;
				}
				return new TTLOption(0, Integer.parseInt(parts[1]), TTLOption.DIFF);
			} else if (parts[1].equals("")) {
				if (! StringUtils.isNumeric(parts[0])) {
					return null;
				}
				return new TTLOption(Integer.parseInt(parts[0]), 255, TTLOption.DIFF);
			}
			if (! StringUtils.isNumeric(parts[0]) || ! StringUtils.isNumeric(parts[1])) {
				return null;
			}
			return new TTLOption(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), TTLOption.DIFF);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new TTLOption(Integer.parseInt(option.trim()));
	}
	
	private int ttl;
	private int ttl2;
	private int code;
	
	public TTLOption(int ttl) {
		this.ttl = ttl;
		this.ttl2 = -1;
		this.code = -1;
	}
	
	public TTLOption(int ttl, int code) {
		this.ttl = ttl;
		this.ttl2 = -1;
		this.code = code;
	}
	
	public TTLOption(int ttl, int ttl2, int code) {
		this.ttl = ttl;
		this.ttl2 = ttl2;
		this.code = code;
	}

	public boolean checkTTL(int ttl) {
		switch (this.code) {
			case TTLOption.LT:
				return ttl < this.ttl;
			case TTLOption.GT:
				return ttl > this.ttl;
			case TTLOption.LTE:
				return ttl <= this.ttl;
			case TTLOption.GTE:
				return ttl >= this.ttl;
			case TTLOption.DIFF:
				return this.ttl <= ttl && ttl <= this.ttl2;
			default:
				return ttl == this.ttl;
		}
	}
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			IPHeader header = (IPHeader) p.getHeader();
			return this.checkTTL(Byte.toUnsignedInt(header.getTimeToLive()));
		}
		return false;
	}
}
