package lib.options;

public class SameIPOption {

	public static final String SAMEIP = "sameip";
	
	private boolean sameip;
	
	public SameIPOption(boolean sameip) {
		this.sameip = sameip;
	}
	
	public boolean checkSameIP() {
		return this.sameip;
	}
}
