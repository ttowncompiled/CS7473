package util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GeneratorCLI {
	
	public static final String INPUT = "r";
	public static final String DEV = "dev";
	public static final String HELP = "help";
	public static final String SHOW = "show";

	private String name;
	private Options options;
	private CommandLine cmd;

	@SuppressWarnings({ "static-access", "deprecation" })
	public GeneratorCLI(String name, String[] args) throws ParseException {
		this.name = name;
		this.options = new Options();
		this.options.addOption(OptionBuilder.hasArg().withArgName("filename").withDescription("Read packets from file. Program reads packets from the network by default.").isRequired().create(GeneratorCLI.INPUT));
		this.options.addOption(OptionBuilder.hasArg().withArgName("name").withDescription("The name of the network adapter to sniff. Otherwise, a network adapter is chosen by default.").create(GeneratorCLI.DEV));
		this.options.addOption(OptionBuilder.withDescription("Prints usage information.").create(GeneratorCLI.HELP));
		this.options.addOption(OptionBuilder.withDescription("Prints available network adapters.").create(GeneratorCLI.SHOW));
		this.cmd = this.parse(args);
	}
	
	private CommandLine parse(String[] args) throws ParseException {
		return new DefaultParser().parse(this.options, args);
	}
	
	public boolean hasInput() {
		return this.cmd.hasOption(SnifferCLI.INPUT);
	}
	
	public String getInput() {
		return this.cmd.getOptionValue(SnifferCLI.INPUT);
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
