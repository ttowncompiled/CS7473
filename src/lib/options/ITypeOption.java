package lib.options;

import org.apache.commons.lang3.StringUtils;

public class ITypeOption {

	public static final String ITYPE = "itype";
	
	private static final int LT = 1;
	private static final int GT = 2;
	private static final int DIFF = 5;
	
	public static ITypeOption parseOption(String option) {
		if (option.charAt(0) == '<') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new ITypeOption(Integer.parseInt(option.substring(1).trim()), ITypeOption.LT);
		} else if (option.charAt(0) == '>') {
			if (! StringUtils.isNumeric(option.substring(1).trim())) {
				return null;
			}
			return new ITypeOption(Integer.parseInt(option.substring(1).trim()), ITypeOption.GT);
		}
		if (option.contains("<>")) {
			String[] parts = option.trim().split("<>");
			if (parts[0] == "" || parts[1] == "") {
				return null;
			}
			if (! StringUtils.isNumeric(parts[0]) || ! StringUtils.isNumeric(parts[1])) {
				return null;
			}
			return new ITypeOption(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), ITypeOption.DIFF);
		}
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new ITypeOption(Integer.parseInt(option.trim()));
	}
	
	private int itype;
	private int itype2;
	private int code;
	
	public ITypeOption(int itype) {
		this.itype = itype;
		this.itype2 = -1;
		this.code = -1;
	}
	
	public ITypeOption(int itype, int code) {
		this.itype = itype;
		this.itype2 = -1;
		this.code = code;
	}
	
	public ITypeOption(int itype, int itype2, int code) {
		this.itype = itype;
		this.itype2 = itype2;
		this.code = code;
	}
	
	public boolean checkIType(int itype) {
		switch (this.code) {
			case ITypeOption.LT:
				return itype < this.itype;
			case ITypeOption.GT:
				return itype > this.itype;
			case ITypeOption.DIFF:
				return this.itype <= itype && itype <= this.itype2;
			default:
				return itype == this.itype;
		}
	}
}
