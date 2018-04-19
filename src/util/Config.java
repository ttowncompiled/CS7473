package util;

public class Config {
	
	public static final String NULL = "N/A";
	public static final String ETHERNET = "eth";
	public static final String ARP = "arp";
	public static final String IP = "ip";
	public static final String ICMP = "icmp";
	public static final String TCP = "tcp";
	public static final String UDP = "udp";
	
	public static final int ETH_IP_ETHERTYPE = 0x0800;
	public static final int ETH_ARP_ETHERTYPE = 0x0806;
	
	public static final int IP_ICMP_PROTOCOL = 1;
	public static final int IP_TCP_PROTOCOL = 6;
	public static final int IP_UDP_PROTOCOL = 17;
	
	public static final String FTP_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\ftp.dat";
	public static final String HTTP_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\http.dat";
	public static final String PING_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\ping.dat";
	public static final String TELNET_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\telnet.dat";
	public static final String RULES_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\rules.txt";
	
	public static final String WINNUKE_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\winnuke.dat";
	public static final String JOLTUDP_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\joltUDP.dat";
	public static final String TEARDROP_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\teardrop.dat";
	public static final String JOLTPACKET_PATH = "C:\\Users\\ianri\\Workspace\\CS7473\\resources\\jolt2packet.dat";
	
	public static final int IP_MAX_LENGTH = 65548;
}
