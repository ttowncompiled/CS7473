package lib;

import java.util.ArrayList;

import lib.packets.Packet;

public class Triple implements Showable {
	
	public static int ARP = 0;
	public static int IP_NO_OVERLAP = 1;
	public static int IP_OVERLAP = 2;
	public static int IP_TOO_LARGE = 3;
	public static int TIME_OUT = 4;

	private int sid;
	private Packet datagram;
	private ArrayList<Packet> fragments;
	
	public static Triple ARPTriple(Packet packet) {
		return new Triple(Triple.ARP, packet, packet);
	}
	
	public static Triple NoOverlapTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_NO_OVERLAP, packet, fragments);
	}
	
	public static Triple OverlapTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_OVERLAP, packet, fragments);
	}
	
	public static Triple TooLargeTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_TOO_LARGE, packet, fragments);
	}
	
	public static Triple TimeOutTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.TIME_OUT, packet, fragments);
	}
	
	public Triple(int sid, Packet datagram, Packet fragment) {
		this.sid = sid;
		this.datagram = datagram;
		this.fragments = new ArrayList<>();
		this.fragments.add(fragment);
	}
	
	public Triple(int sid, Packet datagram, ArrayList<Packet> fragments) {
		this.sid = sid;
		this.datagram = datagram;
		this.fragments = fragments;
	}
	
	public int getID() {
		return this.sid;
	}
	
	public Packet getDatagram() {
		return this.datagram;
	}
	
	public ArrayList<Packet> getFragments() {
		return this.fragments;
	}
	
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		rep.append("SID=").append(this.sid).append("\n");
		rep.append(this.datagram.toString()).append("\n");
		if (this.fragments != null && ! this.fragments.isEmpty()) {
			rep.append(this.fragments.get(0).toString());
			for (int i = 1; i < this.fragments.size(); i++) {
				rep.append("\n").append(this.fragments.get(i));
			}
		}
		return rep.toString();
	}
	
	public void show() {
		System.out.println(this.toString());
	}
}
