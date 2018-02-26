package lib.headers;

import lib.Showable;

public abstract class Header implements Showable {

	public String type;
	
	public Header(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	@Override
	public abstract String toString();
	
	public void show() {
		System.out.println(this.toString());
	}
}