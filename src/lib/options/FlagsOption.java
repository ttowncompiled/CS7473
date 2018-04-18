package lib.options;

public class FlagsOption {
	
	public static final String FLAGS = "flags";
	
	private static final String FIN = "F";
	private static final String SYN = "S";
	private static final String RST = "R";
	private static final String PSH = "P";
	private static final String ACK = "A";
	private static final String URG = "U";
	private static final String CWR = "C";
	private static final String ECE = "E";
	private static final String NONE = "0";
	
	private static final int AND = 6;
	private static final int OR = 7;
	private static final int NOT = 8;
	
	public static FlagsOption parseOption(String option) {
		boolean fin = false;
		boolean syn = false;
		boolean rst = false;
		boolean psh = false;
		boolean ack = false;
		boolean urg = false;
		boolean cwr = false;
		boolean ece = false;
		boolean none = false;
		if (option.contains(FlagsOption.FIN)) {
			fin = true;
		}
		if (option.contains(FlagsOption.SYN)) {
			syn = true;
		}
		if (option.contains(FlagsOption.RST)) {
			psh = true;
		}
		if (option.contains(FlagsOption.PSH)) {
			psh = true;
		}
		if (option.contains(FlagsOption.ACK)) {
			ack = true;
		}
		if (option.contains(FlagsOption.URG)) {
			urg = true;
		}
		if (option.contains(FlagsOption.CWR)) {
			cwr = true;
		}
		if (option.contains(FlagsOption.ECE)) {
			ece = true;
		}
		if (option.contains(FlagsOption.NONE)) {
			none = true;
		}
		if (option.contains("+")) {
			return new FlagsOption(fin, syn, rst, psh, ack, urg, cwr, ece, none, FlagsOption.AND);
		} else if (option.contains("*")) {
			return new FlagsOption(fin, syn, rst, psh, ack, urg, cwr, ece, none, FlagsOption.OR);
		} else if (option.contains("!")) {
			return new FlagsOption(fin, syn, rst, psh, ack, urg, cwr, ece, none, FlagsOption.NOT);
		}
		return new FlagsOption(fin, syn, rst, psh, ack, urg, cwr, ece, none, FlagsOption.AND);
	}
	
	private boolean fin;
	private boolean syn;
	private boolean rst;
	private boolean psh;
	private boolean ack;
	private boolean urg;
	private boolean cwr;
	private boolean ece;
	private boolean none;
	private int code;
	
	public FlagsOption(boolean fin, boolean syn, boolean rst, boolean psh, boolean ack, boolean urg, boolean cwr, boolean ece, boolean none, int code) {
		this.fin = fin;
		this.syn = syn;
		this.rst = rst;
		this.psh = psh;
		this.ack = ack;
		this.urg = urg;
		this.cwr = cwr;
		this.ece = ece;
		this.code = code;
	}
	
	public boolean checkFlags(boolean fin, boolean syn, boolean rst, boolean psh, boolean ack, boolean urg, boolean cwr, boolean ece) {
		if (this.none) {
			return ! fin && ! syn && ! rst && ! psh && ! ack && ! urg && ! cwr && ! ece;
		}
		switch (this.code) {
			case FlagsOption.AND:
				return (this.fin && fin) && (this.syn && syn) && (this.rst && rst) && (this.psh && psh) && (this.ack && ack) && (this.urg && urg) && (this.cwr && cwr) && (this.ece && ece);
			case FlagsOption.OR:
				return (this.fin && fin) || (this.syn && syn) || (this.rst && rst) || (this.psh && psh) || (this.ack && ack) || (this.urg && urg) || (this.cwr && cwr) || (this.ece && ece);
			case FlagsOption.NOT:
				return (this.fin && ! fin) && (this.syn && ! syn) && (this.rst && ! rst) && (this.psh && ! psh) && (this.ack && ! ack) && (this.urg && ! urg) && (this.cwr && ! cwr) && (this.ece && ! ece);
			default:
				return false;
		}
	}
}
