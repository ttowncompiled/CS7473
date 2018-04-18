package lib.options;

public class FragBitsOption {

	public static final String FRAGBITS = "fragbits";
	
	private static final String MORE = "M";
	private static final String DONT = "D";
	private static final String RES = "R";
	
	private static final int AND = 6;
	private static final int OR = 7;
	private static final int NOT = 8;
	
	public static FragBitsOption parseOption(String option) {
		boolean more = false;
		boolean dont = false;
		boolean res = false;
		if (option.contains(FragBitsOption.MORE)) {
			more = true;
		}
		if (option.contains(FragBitsOption.DONT)) {
			dont = true;
		}
		if (option.contains(FragBitsOption.RES)) {
			res = true;
		}
		if (option.contains("+")) {
			return new FragBitsOption(more, dont, res, FragBitsOption.AND);
		} else if (option.contains("*")) {
			return new FragBitsOption(more, dont, res, FragBitsOption.OR);
		} else if (option.contains("!")) {
			return new FragBitsOption(more, dont, res, FragBitsOption.NOT);
		}
		return new FragBitsOption(more, dont, res, FragBitsOption.AND);
	}
	
	private boolean more;
	private boolean dont;
	private boolean res;
	private int code;
	
	public FragBitsOption(boolean more, boolean dont, boolean res, int code) {
		this.more = more;
		this.dont = dont;
		this.res = res;
		this.code = code;
	}
	
	public boolean checkFragBits(boolean more, boolean dont, boolean res) {
		switch (this.code) {
			case FragBitsOption.AND:
				return (this.more && more) && (this.dont && dont) && (this.res && res);
			case FragBitsOption.OR:
				return (this.more && more) || (this.dont && dont) || (this.res && res);
			case FragBitsOption.NOT:
				return (this.more && ! more) && (this.dont && ! dont) && (this.res && ! res);
			default:
				return false;
		}
	}
}
