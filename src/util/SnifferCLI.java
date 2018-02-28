package util;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

public class SnifferCLI {
	
	public static final String COUNT = "c";
	public static final String INPUT = "r";
	public static final String OUTPUT = "o";
	public static final String TYPE = "t";
	public static final String HEADER_INFO = "h";
	public static final String SRC = "src";
	public static final String DEST = "dst";
	public static final String SORD = "sord";
	public static final String SANDD = "sandd";
	public static final String SPORT = "sport";
	public static final String DPORT = "dport";
	public static final String DEV = "dev";
	public static final String HELP = "help";
	public static final String SHOW = "show";
	
	private String name;
	private Options options;
	private CommandLine cmd;

	@SuppressWarnings({ "static-access", "deprecation" })
	public SnifferCLI(String name, String[] args) throws ParseException {
		this.name = name;
		this.options = new Options();
		this.options.addOption(OptionBuilder.hasArg().withArgName("count").withDescription("Exit after receiving count packets.").create(SnifferCLI.COUNT));
		this.options.addOption(OptionBuilder.hasArg().withArgName("filename").withDescription("Read packets from file. Program reads packets from the network by default.").create(SnifferCLI.INPUT));
		this.options.addOption(OptionBuilder.hasArg().withArgName("filename").withDescription("Save output to filename.").create(SnifferCLI.OUTPUT));
		this.options.addOption(OptionBuilder.hasArg().withArgName("type").withDescription("Print only packets of the specified type where type is one of: ['eth', 'arp', 'ip', 'icmp', 'tcp', 'udp'].").create(SnifferCLI.TYPE));
		this.options.addOption(OptionBuilder.withDescription("Print header info only as specified by -t.").create(SnifferCLI.HEADER_INFO));
		this.options.addOption(OptionBuilder.hasArg().withArgName("saddress").withDescription("Print only packets with source address equal to saddress.").create(SnifferCLI.SRC));
		this.options.addOption(OptionBuilder.hasArg().withArgName("daddress").withDescription("Print only packets with destination address equal to daddress").create(SnifferCLI.DEST));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("saddress daddress").withValueSeparator(' ').withDescription("Print only packets where the source address matches saddress or the destination address matches daddress.").create(SnifferCLI.SORD));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("saddress daddress").withValueSeparator(' ').withDescription("Print only packets where the source address mathces saddress and the destination address matches daddress.").create(SnifferCLI.SANDD));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("port1 port2").withValueSeparator(' ').withDescription("Print only packets where the source port is in the range port1-port2.").create(SnifferCLI.SPORT));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("port1 port2").withValueSeparator(' ').withDescription("Print only packets where the destination port is in the range port1-port2.").create(SnifferCLI.DPORT));
		this.options.addOption(OptionBuilder.hasArg().withArgName("name").withDescription("The name of the network adapter to sniff. Otherwise, a network adapter is chosen by default.").create(SnifferCLI.DEV));
		this.options.addOption(OptionBuilder.withDescription("Prints usage information.").create(SnifferCLI.HELP));
		this.options.addOption(OptionBuilder.withDescription("Prints available network adapters.").create(SnifferCLI.SHOW));
		this.cmd = this.parse(args);
	}
	
	private CommandLine parse(String[] args) throws ParseException {
		return new DefaultParser().parse(this.options, args);
	}
	
	public boolean hasCount() {
		return this.cmd.hasOption(SnifferCLI.COUNT);
	}
	
	public int getCount() {
		return StringUtils.isNumeric(this.cmd.getOptionValue(SnifferCLI.COUNT)) ? Integer.parseInt(this.cmd.getOptionValue(SnifferCLI.COUNT)) : 0;
	}
	
	public boolean hasInput() {
		return this.cmd.hasOption(SnifferCLI.INPUT);
	}
	
	public String getInput() {
		return this.cmd.getOptionValue(SnifferCLI.INPUT);
	}
	
	public boolean hasOutput() {
		return this.cmd.hasOption(SnifferCLI.OUTPUT);
	}
	
	public String getOutput() {
		return this.cmd.getOptionValue(SnifferCLI.OUTPUT);
	}
	
	public boolean hasType() {
		return this.cmd.hasOption(SnifferCLI.TYPE);
	}
	
	public boolean hasValidType() {
		String t = this.cmd.getOptionValue(SnifferCLI.TYPE);
		return t.equals(Config.ARP) || t.equals(Config.ETHERNET) || t.equals(Config.ICMP) || t.equals(Config.IP) || t.equals(Config.TCP) || t.equals(Config.UDP);
	}
	
	public String getType() {
		return this.cmd.getOptionValue(SnifferCLI.TYPE);
	}
	
	public boolean hasHeaderInfo() {
		return this.cmd.hasOption(SnifferCLI.HEADER_INFO);
	}
	
	public boolean hasSource() {
		return this.cmd.hasOption(SnifferCLI.SRC);
	}
	
	public boolean hasValidSource() {
		return StringUtils.isNumeric(this.cmd.getOptionValue(SnifferCLI.SRC));
	}
	
	public int getSource() {
		return (int) Long.parseLong(this.cmd.getOptionValue(SnifferCLI.SRC));
	}
	
	public boolean hasDest() {
		return this.cmd.hasOption(SnifferCLI.DEST);
	}
	
	public boolean hasValidDest() {
		return StringUtils.isNumeric(this.cmd.getOptionValue(SnifferCLI.DEST));
	}
	
	public int getDest() {
		return (int) Long.parseLong(this.cmd.getOptionValue(SnifferCLI.DEST));
	}
	
	public boolean hasSourceOrDest() {
		return this.cmd.hasOption(SnifferCLI.SORD);
	}
	
	public boolean hasValidSourceOrDest() {
		String[] sord = this.cmd.getOptionValues(SnifferCLI.SORD);
		return StringUtils.isNumeric(sord[0]) && StringUtils.isNumeric(sord[1]);
	}
	
	public int[] getSourceOrDest() {
		int[] sord = new int[2];
		sord[0] = (int) Long.parseLong(this.cmd.getOptionValues(SnifferCLI.SORD)[0]);
		sord[1] = (int) Long.parseLong(this.cmd.getOptionValues(SnifferCLI.SORD)[1]);
		return sord;
	}
	
	public boolean hasSourceAndDest() {
		return this.cmd.hasOption(SnifferCLI.SANDD);
	}
	
	public boolean hasValidSourceAndDest() {
		String[] sandd = this.cmd.getOptionValues(SnifferCLI.SANDD);
		return StringUtils.isNumeric(sandd[0]) && StringUtils.isNumeric(sandd[1]);
	}
	
	public int[] getSourceAndDest() {
		int[] sandd = new int[2];
		sandd[0] = (int) Long.parseLong(this.cmd.getOptionValues(SnifferCLI.SANDD)[0]);
		sandd[1] = (int) Long.parseLong(this.cmd.getOptionValues(SnifferCLI.SANDD)[1]);
		return sandd;
	}
	
	public boolean hasSourcePort() {
		return this.cmd.hasOption(SnifferCLI.SPORT);
	}
	
	public int getSourcePortStart() {
		String[] sports = this.cmd.getOptionValues(SnifferCLI.SPORT);
		if (sports.length != 2) {
			return -1;
		}
		if (! StringUtils.isNumeric(sports[0])) {
			return -1;
		}
		return Integer.parseInt(sports[0]);
	}
	
	public int getSourcePortEnd() {
		String[] sports = this.cmd.getOptionValues(SnifferCLI.SPORT);
		if (sports.length != 2) {
			return -1;
		}
		if (! StringUtils.isNumeric(sports[1])) {
			return -1;
		}
		return Integer.parseInt(sports[1]);
	}
	
	public boolean hasDestPort() {
		return this.cmd.hasOption(SnifferCLI.DPORT);
	}
	
	public int getDestPortStart() {
		String[] dports = this.cmd.getOptionValues(SnifferCLI.DPORT);
		if (dports.length != 2) {
			return -1;
		}
		if (! StringUtils.isNumeric(dports[0])) {
			return -1;
		}
		return Integer.parseInt(dports[0]);
	}
	
	public int getDestPortEnd() {
		String[] dports = this.cmd.getOptionValues(SnifferCLI.DPORT);
		if (dports.length != 2) {
			return -1;
		}
		if (! StringUtils.isNumeric(dports[1])) {
			return -1;
		}
		return Integer.parseInt(dports[1]);
	}
	
	public boolean hasDev() {
		return this.cmd.hasOption(SnifferCLI.DEV);
	}
	
	public String getDev() {
		return this.cmd.getOptionValue(SnifferCLI.DEV);
	}
	
	public boolean hasHelp() {
		return this.cmd.hasOption(SnifferCLI.HELP);
	}
	
	public boolean hasShow() {
		return this.cmd.hasOption(SnifferCLI.SHOW);
	}
	
	public void help() {
		new HelpFormatter().printHelp(this.name, this.options);
	}
}
