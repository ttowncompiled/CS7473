package lib.options;

import org.apache.commons.lang3.StringUtils;

public class SeqOption {

	public static final String SEQ = "seq";
	
	public static SeqOption parseOption(String option) {
		if (! StringUtils.isNumeric(option.trim())) {
			return null;
		}
		return new SeqOption(Integer.parseInt(option.trim()));
	}
	
	private int seq;
	
	public SeqOption(int seq) {
		this.seq = seq;
	}
	
	public boolean checkSeq(int seq) {
		return seq == this.seq;
	}
}
