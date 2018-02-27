package lib.headers;

import util.Config;
import util.HexString;

public class NullHeader extends Header {
	
	public static final int MAX_BITS = 0;
	public static final int MAX_HEX = 0;
	
	public static final NullHeader NULL_HEADER = new NullHeader();

	public NullHeader() {
		super(Config.NULL);
	}
	
	public int getHeaderBitLength() {
		return 0;
	}
	
	public int getHeaderHexLength() {
		return 0;
	}
	
	public HexString toHexString() {
		return new HexString("");
	}
	
	public String toString() {
		return "";
	}
}
