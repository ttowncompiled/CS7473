package lib.options;

public class IPOptsOption {
	
	public static final String IPOPTION = "ipoption";
	
	private static final String RR = "rr";
	private static final String EOL = "eol";
	private static final String NOP = "nop";
	private static final String TS = "ts";
	private static final String SEC = "sec";
	private static final String ESEC = "esec";
	private static final String LSRR = "lsrr";
	private static final String LSRRE = "lsrre";
	private static final String SSRR = "ssrr";
	private static final String SATID = "satid";
	private static final String ANY = "any";
	
	public static IPOptsOption parseOption(String option) {
		if (option.trim().equals("")) {
			return null;
		}
		if (option.contains(IPOptsOption.LSRRE)) {
			return new IPOptsOption(false, false, false, false, false, false, false, true, false, false);
		} else if (option.contains(IPOptsOption.LSRR)) {
			return new IPOptsOption(false, false, false, false, false, false, true, false, false, false);
		} else if (option.contains(IPOptsOption.SSRR)) {
			return new IPOptsOption(false, false, false, false, false, false, false, false, true, false);
		} else if (option.contains(IPOptsOption.RR)) {
			return new IPOptsOption(true, false, false, false, false, false, false, false, false, false);
		} else if (option.contains(IPOptsOption.ESEC)) {
			return new IPOptsOption(false, false, false, false, false, true, false, false, false, false);
		} else if (option.contains(IPOptsOption.SEC)) {
			return new IPOptsOption(false, false, false, false, true, false, false, false, false, false);
		} else if (option.contains(IPOptsOption.EOL)) {
			return new IPOptsOption(false, true, false, false, false, false, false, false, false, false);
		} else if (option.contains(IPOptsOption.NOP)) {
			return new IPOptsOption(false, false, true, false, false, false, false, false, false, false);
		} else if (option.contains(IPOptsOption.TS)) {
			return new IPOptsOption(false, false, false, true, false, false, false, false, false, false);
		} else if (option.contains(IPOptsOption.SATID)) {
			return new IPOptsOption(false, false, false, false, false, false, false, false, false, true);
		} else if (option.contains(IPOptsOption.ANY)) {
			return new IPOptsOption();
		}
		return null;
	}
	
	private boolean rr;
	private boolean eol;
	private boolean nop;
	private boolean ts;
	private boolean sec;
	private boolean esec;
	private boolean lsrr;
	private boolean lsrre;
	private boolean ssrr;
	private boolean satid;
	private boolean any;
	
	public IPOptsOption(boolean rr, boolean eol, boolean nop, boolean ts, boolean sec, boolean esec, boolean lsrr, boolean lsrre, boolean ssrr, boolean satid) {
		this.rr = rr;
		this.eol = eol;
		this.nop = nop;
		this.ts = ts;
		this.sec = sec;
		this.esec = esec;
		this.lsrr = lsrr;
		this.lsrre = lsrre;
		this.ssrr = ssrr;
		this.satid = satid;
	}
	
	public IPOptsOption() {
		this.any = true;
	}
	
	public boolean checkIPOpts(boolean rr, boolean eol, boolean nop, boolean ts, boolean sec, boolean esec, boolean lsrr, boolean lsrre, boolean ssrr, boolean satid) {
		if (this.any) {
			return rr || eol || nop || ts || sec || esec || lsrr || lsrre || ssrr || satid;
		}
		return (this.rr && rr) || (this.eol && eol) || (this.nop && nop) || (this.ts && ts) || (this.sec && sec) || (this.esec && esec) || (this.lsrr && lsrr) || (this.lsrre && lsrre) || (this.ssrr && ssrr) || (this.satid && satid);
	}
}
