package lib.options;

import org.apache.commons.lang3.StringUtils;

public class ICodeOption {

	public static final String ICODE = "icode";
	
	private static final int LT = 1;
	private static final int GT = 2;
	private static final int DIFF = 5;
	
	public static ICodeOption parseOption(String option) {
		if (option.charAt(0) == '<') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new ICodeOption(Integer.parseInt(option.substring(1).trim()), ICodeOption.LT);
		} else if (option.charAt(0) == '>') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new ICodeOption(Integer.parseInt(option.substring(1).trim()), ICodeOption.GT);
		}
		if (option.contains("<>")) {
			String[] parts = option.trim().split("<>");
			if (parts[0] == "" || parts[1] == "") {
				return null;
			}
			if (! StringUtils.isNumeric(parts[0]) || ! StringUtils.isNumeric(parts[1])) {
				return null;
			}
			return new ICodeOption(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), ICodeOption.DIFF);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new ICodeOption(Integer.parseInt(option.trim()));
	}
	
	private int icode;
	private int icode2;
	private int code;
	
	public ICodeOption(int itype) {
		this.icode = itype;
		this.icode2 = -1;
		this.code = -1;
	}
	
	public ICodeOption(int itype, int code) {
		this.icode = itype;
		this.icode2 = -1;
		this.code = code;
	}
	
	public ICodeOption(int itype, int itype2, int code) {
		this.icode = itype;
		this.icode2 = itype2;
		this.code = code;
	}
	
	public boolean checkICode(int icode) {
		switch (this.code) {
			case ICodeOption.LT:
				return icode < this.icode;
			case ICodeOption.GT:
				return icode > this.icode;
			case ICodeOption.DIFF:
				return this.icode <= icode && icode <= this.icode2;
			default:
				return icode == this.icode;
		}
	}
}
