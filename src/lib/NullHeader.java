package lib;

import util.Config;

public class NullHeader extends Header {
	
	public static final NullHeader NULL_HEADER = new NullHeader();

	public NullHeader() {
		super(Config.NULL);
	}
	
	@Override
	public String toString() {
		return "";
	}
}
