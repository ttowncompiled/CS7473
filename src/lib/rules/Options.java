package lib.rules;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import lib.Showable;

public class Options implements Showable {
	
	public static final String MSG = "msg";
	public static final String LOG_TO = "logto";
	public static final String TTL = "ttl";
	public static final String TOS = "tos";
	public static final String IP_ID = "id";
	public static final String FRAG_OFF = "fragoffset";
	public static final String IP_OPTION = "ipoption";
	public static final String FRAG_BITS = "fragbits";
	public static final String D_SIZE = "dsize";
	public static final String FLAGS = "flags";
	public static final String SEQ = "seq";
	public static final String ACK = "ack";
	public static final String I_TYPE = "itype";
	public static final String I_CODE = "icode";
	public static final String CONTENT = "content";
	public static final String SAME_IP = "sameip";
	public static final String SID = "sid";
	
	public static Options parseOptions(String options) {
		if (options.charAt(0) != '(' && options.charAt(options.length()-1) != ')') {
			return null;
		}
		String[] opts = options.substring(1, options.length()-1).split(";");
		HashMap<String, String> args = new HashMap<>();
		for (int i = 0; i < opts.length; i++) {
			if (! opts[i].contains(":")) {
				continue;
			}
			String[] parts = opts[i].split(":");
			String param = parts[0].trim();
			String arg = parts[1].trim();
			args.put(param, arg);
		}
		return ! args.isEmpty() ? new Options(args) : null;
	}
	
	private HashMap<String, String> options;
	
	public Options(HashMap<String, String> options) {
		this.options = options;
	}
	
	public HashMap<String, String> getOptions() {
		return this.options;
	}
	
	public boolean hasMsg() {
		return this.options.containsKey(Options.MSG);
	}
	
	public String getMsg() {
		return this.options.get(Options.MSG);
	}
	
	public boolean hasLogTo() {
		return this.options.containsKey(Options.LOG_TO);
	}
	
	public String getLogTo() {
		return this.options.get(Options.LOG_TO);
	}
	
	public boolean hasValidTTL() {
		return this.options.containsKey(Options.TTL) && StringUtils.isNumeric(this.options.get(Options.TTL));
	}
	
	public int getTTL() {
		return Integer.parseInt(this.options.get(Options.TTL));
	}
	
	public boolean hasValidTOS() {
		return this.options.containsKey(Options.TOS) && StringUtils.isNumeric(this.options.get(Options.TOS));
	}
	
	public int getTOS() {
		return Integer.parseInt(this.options.get(Options.TOS));
	}
	
	public boolean hasValidIPID() {
		return this.options.containsKey(Options.IP_ID) && StringUtils.isNumeric(this.options.get(Options.IP_ID));
	}
	
	public int getIPID() {
		return Integer.parseInt(this.options.get(Options.IP_ID));
	}
	
	public boolean hasValidDSize() {
		return this.options.containsKey(Options.D_SIZE) && StringUtils.isNumeric(this.options.get(Options.D_SIZE));
	}
	
	public int getDSize() {
		return Integer.parseInt(this.options.get(Options.D_SIZE));
	}
	
	public boolean hasValidSeq() {
		return this.options.containsKey(Options.SEQ) && StringUtils.isNumeric(this.options.get(Options.SEQ));
	}
	
	public int getSeq() {
		return Integer.parseInt(this.options.get(Options.SEQ));
	}
	
	public boolean hasValidAck() {
		return this.options.containsKey(Options.ACK) && StringUtils.isNumeric(this.options.get(Options.ACK));
	}
	
	public int getAck() {
		return Integer.parseInt(this.options.get(Options.ACK));
	}
	
	public boolean hasValidIType() {
		return this.options.containsKey(Options.I_TYPE) && StringUtils.isNumeric(this.options.get(Options.I_TYPE));
	}
	
	public int getIType() {
		return Integer.parseInt(this.options.get(Options.I_TYPE));
	}
	
	public boolean hasValidICode() {
		return this.options.containsKey(Options.I_CODE) && StringUtils.isNumeric(this.options.get(Options.I_CODE));
	}
	
	public int getICode() {
		return Integer.parseInt(this.options.get(Options.I_CODE));
	}

	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder().append("(");
		for (String param : this.options.keySet()) {
			rep.append(param).append(": ").append(this.options.get(param)).append("; ");
		}
		return rep.deleteCharAt(rep.length()-1).append(")").toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
