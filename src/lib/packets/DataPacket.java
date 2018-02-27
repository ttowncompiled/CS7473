package lib.packets;

import util.HexString;
import lib.headers.NullHeader;

public class DataPacket extends Packet {
	
	private HexString data;
	
	public DataPacket(HexString data) {
		super(NullHeader.NULL_HEADER, null);
		this.data = data;
	}
	
	public HexString getData() {
		return this.data;
	}
	
	private static String separator() {
		StringBuilder rep = new StringBuilder();
		
		rep.append("+");
		for (int i = 0; i < 63; i++) {
			rep.append(i % 2 == 0 ? "-" : "+");
		}
		rep.append("+");
		
		return rep.toString();
	}
	
	@Override
	public String toString() {
		String S = this.data.toString();
		StringBuilder rep = new StringBuilder();
		rep.append("|");
		for (int i = 0; i < 63 && i < S.length(); i++) {
			if (i % 2 == 0) {
				rep.append(S.charAt(i/2));
			} else {
				rep.append(" ");
			}
		}
		if (S.length() > 32) {
			for (int k = 1; k <= S.length()/32-1; k++) {
				rep.append("\n ");
				for (int i = 0; i < 63; i++) {
					if (i % 2 == 0) {
						rep.append(S.charAt(32*k+i/2));
					} else {
						rep.append(" ");
					}
				}
			}
		}
		if (S.length() % 32 > 0) {
			rep.append("\n ");
			int offset = S.length() - (S.length() % 32);
			for (int i = 0; i < 63; i++) {
				if (i % 2 == 1) {
					rep.append(" ");
				} else if (i % 2 == 0 && offset + i/2 < S.length()) {
					rep.append(S.charAt(offset+i/2));
				} else {
					rep.append("x");
				}
			}
		}
		rep.append("|\n");
		rep.append(DataPacket.separator());
		return rep.toString();
	}
}
