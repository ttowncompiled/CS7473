package lib.headers;

import lib.Showable;
import util.HexString;

public abstract class Header implements Showable {

	public String type;
	
	public Header(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public abstract int getHeaderBitLength();
	
	public abstract int getHeaderByteLength();
	
	public abstract int getHeaderHexLength();
	
	public abstract HexString toHexString();
	
	@Override
	public abstract String toString();
	
	public void show() {
		System.out.println(this.toString());
	}
}
