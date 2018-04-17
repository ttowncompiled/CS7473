package lib.rules;

import lib.Showable;

public class DirectionRule implements Showable {
	
	public static final String UNI = "->";
	public static final String BI = "<>";
	
	public static DirectionRule parseDirection(String direction) {
		if (! direction.equals(DirectionRule.UNI) && ! direction.equals(DirectionRule.BI)) {
			return null;
		}
		return new DirectionRule(direction);
	}
	
	private String direction;
	
	public DirectionRule(String direction) {
		this.direction = direction;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public boolean isUni() {
		return this.direction.equals(DirectionRule.UNI);
	}
	
	public boolean isBi() {
		return this.direction.equals(DirectionRule.BI);
	}
	
	@Override
	public String toString() {
		return this.direction;
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
