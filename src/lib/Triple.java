package lib;

import java.util.ArrayList;

import lib.packets.Packet;

public class Triple {
	
	public static int ARP = 0;
	public static int IP_NO_OVERLAP = 1;
	public static int IP_OVERLAP = 2;
	public static int IP_TOO_LARGE = 3;
	public static int TIME_OUT = 4;

	private int sid;
	private Packet datagram;
	private ArrayList<Packet> fragments;
	
	public Triple ARPTriple(Packet packet) {
		return new Triple(Triple.ARP, packet, packet);
	}
	
	public Triple NoOverlapTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_NO_OVERLAP, packet, fragments);
	}
	
	public Triple OverlapTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_OVERLAP, packet, fragments);
	}
	
	public Triple TooLargeTriple(Packet packet, ArrayList<Packet> fragments) {
		return new Triple(Triple.IP_TOO_LARGE, packet, fragments);
	}
	
	public Triple TimeOutTriple(Packet packet, ArrayList<Packet> fragments) {
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
}
