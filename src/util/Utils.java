package util;

import org.apache.commons.lang3.StringUtils;

public class Utils {

	public static int convertIPAddress(String addr) {
		if (StringUtils.isNumeric(addr)) {
			return (int) Long.parseLong(addr);
		}
		String[] address = addr.split("\\.");
		BitString bits = BitString.fromByte((byte) Integer.parseInt(address[0]));
		for (int i = 1; i < address.length; i++) {
			bits = bits.concat(BitString.fromByte((byte) Integer.parseInt(address[i])));
		}
		return bits.toInt();
	}
	
	public static String convertToIPAddress(int addr) {
		BitString bits = BitString.fromInt(addr);
		StringBuilder rep = new StringBuilder();
		rep.append(bits.substring(0, 8).toInt());
		for (int i = 1; i < 4; i++) {
			rep.append(".").append(bits.substring(8*i, 8*(i+1)).toInt());
		}
		return rep.toString();
	}
}
