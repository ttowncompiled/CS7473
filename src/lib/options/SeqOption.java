package lib.options;

import org.apache.commons.lang3.StringUtils;

import lib.headers.TCPHeader;
import lib.packets.Packet;
import util.Config;

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
	
	public boolean checkPacket(Packet p) {
		if (p.getType().equals(Config.IP)) {
			p = p.getNext();
		}
		if (p.getType().equals(Config.TCP)) {
			TCPHeader header = (TCPHeader) p.getHeader();
			return this.checkSeq(header.getSequenceNumber());
		}
		return false;
	}
}
