package lib.rules;

import java.util.HashMap;

import lib.Showable;
import lib.options.*;
import lib.packets.IPPacket;

public class Options implements Showable {
	
	public static final String CONTENT = "content";
	
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
	private AckOption ack;
	private DSizeOption dsize;
	private FlagsOption flags;
	private FragBitsOption fragbits;
	private FragOffsetOption fragoffset;
	private ICodeOption icode;
	private IDOption ipid;
	private IPOptsOption ipopts;
	private ITypeOption itype;
	private LogToOption logto;
	private MsgOption msg;
	private SameIPOption sameip;
	private SeqOption seq;
	private SIDOption sid;
	private TOSOption tos;
	private TTLOption ttl;
	
	public Options(HashMap<String, String> options) {
		this.options = options;
		if (this.options.containsKey(AckOption.ACK)) {
			this.ack = AckOption.parseOption(this.options.get(AckOption.ACK));
		}
		if (this.options.containsKey(DSizeOption.DSIZE)) {
			this.dsize = DSizeOption.parseOption(this.options.get(DSizeOption.DSIZE));
		}
		if (this.options.containsKey(FlagsOption.FLAGS)) {
			this.flags = FlagsOption.parseOption(this.options.get(FlagsOption.FLAGS));
		}
		if (this.options.containsKey(FragBitsOption.FRAGBITS)) {
			this.fragbits = FragBitsOption.parseOption(this.options.get(FragBitsOption.FRAGBITS));
		}
		if (this.options.containsKey(FragOffsetOption.FRAGOFF)) {
			this.fragoffset = FragOffsetOption.parseOption(this.options.get(FragOffsetOption.FRAGOFF));
		}
		if (this.options.containsKey(ICodeOption.ICODE)) {
			this.icode = ICodeOption.parseOption(this.options.get(ICodeOption.ICODE));
		}
		if (this.options.containsKey(IDOption.IPID)) {
			this.ipid = IDOption.parseOption(this.options.get(IDOption.IPID));
		}
		if (this.options.containsKey(IPOptsOption.IPOPTION)) {
			this.ipopts = IPOptsOption.parseOption(this.options.get(IPOptsOption.IPOPTION));
		}
		if (this.options.containsKey(ITypeOption.ITYPE)) {
			this.itype = ITypeOption.parseOption(this.options.get(ITypeOption.ITYPE));
		}
		if (this.options.containsKey(LogToOption.LOGTO)) {
			this.logto = LogToOption.parseOption(this.options.get(LogToOption.LOGTO));
		}
		if (this.options.containsKey(MsgOption.MSG)) {
			this.msg = MsgOption.parseOption(this.options.get(MsgOption.MSG));
		}
		if (this.options.containsKey(SameIPOption.SAMEIP)) {
			this.sameip = new SameIPOption(true);
		}
		if (this.options.containsKey(SeqOption.SEQ)) {
			this.seq = SeqOption.parseOption(this.options.get(SeqOption.SEQ));
		}
		if (this.options.containsKey(SIDOption.SID)) {
			this.sid = SIDOption.parseOption(this.options.get(SIDOption.SID));
		}
		if (this.options.containsKey(TOSOption.TOS)) {
			this.tos = TOSOption.parseOption(this.options.get(TOSOption.TOS));
		}
		if (this.options.containsKey(TTLOption.TTL)) {
			this.ttl = TTLOption.parseOption(this.options.get(TTLOption.TTL));
		}
	}
	
	public HashMap<String, String> getOptions() {
		return this.options;
	}
	
	public boolean checkPacket(IPPacket p) {
		return false;
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
