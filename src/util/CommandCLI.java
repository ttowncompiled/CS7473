package util;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

public class CommandCLI {
	
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
	public static final String HELP = "help";
	
	private String name;
	private Options options;
	private CommandLine cmd;

	@SuppressWarnings({ "static-access", "deprecation" })
	public CommandCLI(String name, String[] args) throws ParseException {
		this.name = name;
		this.options = new Options();
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("count").withDescription("Exit after receiving count packets.").create(CommandCLI.COUNT));
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("filename").withDescription("Read packets from file. Program reads packets from the network by default.").create(CommandCLI.INPUT));
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("filename").withDescription("Save output to filename.").create(CommandCLI.OUTPUT));
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("type").withDescription("Print only packets of the specified type where type is one of: ['eth', 'arp', 'ip', 'icmp', 'tcp', 'udp'].").create(CommandCLI.TYPE));
		this.options.addOption(OptionBuilder.withDescription("Print header info only as specified by -t.").create(CommandCLI.HEADER_INFO));
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("saddress").withDescription("Print only packets with source address equal to saddress.").create(CommandCLI.SRC));
		this.options.addOption(OptionBuilder.hasArgs(1).withArgName("daddress").withDescription("Print only packets with destination address equal to daddress").create(CommandCLI.DEST));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("saddress daddress").withValueSeparator(' ').withDescription("Print only packets where the source address matches saddress or the destination address matches daddress.").create(CommandCLI.SORD));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("saddress daddress").withValueSeparator(' ').withDescription("Print only packets where the source address mathces saddress and the destination address matches daddress.").create(CommandCLI.SANDD));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("port1 port2").withValueSeparator(' ').withDescription("Print only packets where the source port is in the range port1-port2.").create(CommandCLI.SPORT));
		this.options.addOption(OptionBuilder.hasArgs(2).withArgName("port1 port2").withValueSeparator(' ').withDescription("Print only packets where the destination port is in the range port1-port2.").create(CommandCLI.DPORT));
		this.options.addOption(OptionBuilder.withDescription("Prints usage information.").create(CommandCLI.HELP));
		this.cmd = this.parse(args);
	}
	
	private CommandLine parse(String[] args) throws ParseException {
		return new DefaultParser().parse(this.options, args);
	}
	
	public boolean hasCount() {
		return this.cmd.hasOption(CommandCLI.COUNT);
	}
	
	public int getCount() {
		return StringUtils.isAlphanumeric(this.cmd.getOptionValue(CommandCLI.COUNT)) ? Integer.parseInt(this.cmd.getOptionValue(CommandCLI.COUNT)) : 0;
	}
	
	public boolean hasInput() {
		return this.cmd.hasOption(CommandCLI.INPUT);
	}
	
	public String getInput() {
		return this.cmd.getOptionValue(CommandCLI.INPUT);
	}
	
	public boolean hasOutput() {
		return this.cmd.hasOption(CommandCLI.OUTPUT);
	}
	
	public String getOutput() {
		return this.cmd.getOptionValue(CommandCLI.OUTPUT);
	}
	
	public boolean hasType() {
		return this.cmd.hasOption(CommandCLI.TYPE);
	}
	
	public boolean hasValidType() {
		return false;
	}
	
	public String getType() {
		return this.cmd.getOptionValue(CommandCLI.TYPE);
	}
	
	public boolean hasHeaderInfo() {
		return this.cmd.hasOption(CommandCLI.HEADER_INFO);
	}
	
	public boolean hasSource() {
		return this.cmd.hasOption(CommandCLI.SRC);
	}
	
	public String getSource() {
		return this.cmd.getOptionValue(CommandCLI.SRC);
	}
	
	public boolean hasDest() {
		return this.cmd.hasOption(CommandCLI.DEST);
	}
	
	public String getDest() {
		return this.cmd.getOptionValue(CommandCLI.DEST);
	}
	
	public boolean hasSourceOrDest() {
		return this.cmd.hasOption(CommandCLI.SORD);
	}
	
	public String[] getSourceOrDest() {
		return this.cmd.getOptionValues(CommandCLI.SORD);
	}
	
	public boolean hasSourceAndDest() {
		return this.cmd.hasOption(CommandCLI.SANDD);
	}
	
	public String[] getSourceAndDest() {
		return this.cmd.getOptionValues(CommandCLI.SANDD);
	}
	
	public boolean hasSourcePort() {
		return this.cmd.hasOption(CommandCLI.SPORT);
	}
	
	public int[] getSourcePort() {
		String[] sports = this.cmd.getOptionValues(CommandCLI.SPORT);
		if (sports.length != 2) {
			return null;
		}
		if (! StringUtils.isAlphanumeric(sports[0]) || ! StringUtils.isAlphanumeric(sports[1])) {
			return null;
		}
		int[] ports = new int[2];
		ports[0] = Integer.parseInt(sports[0]);
		ports[1] = Integer.parseInt(sports[1]);
		return ports;
	}
	
	public boolean hasDestPort() {
		return this.cmd.hasOption(CommandCLI.DPORT);
	}
	
	public int[] getDestPort() {
		String[] dports = this.cmd.getOptionValues(CommandCLI.DPORT);
		if (dports.length != 2) {
			return null;
		}
		if (! StringUtils.isAlphanumeric(dports[0]) || ! StringUtils.isAlphanumeric(dports[1])) {
			return null;
		}
		int[] ports = new int[2];
		ports[0] = Integer.parseInt(dports[0]);
		ports[1] = Integer.parseInt(dports[1]);
		return ports;
	}
	
	public boolean hasHelp() {
		return this.cmd.hasOption(CommandCLI.HELP);
	}
	
	public void help() {
		new HelpFormatter().printHelp(this.name, this.options);
	}
}
