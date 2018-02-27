package main;

import org.apache.commons.cli.ParseException;

import util.*;

public class Sniffer {
	
	public static final String NAME = "Sniffer";

	public static void main(String[] args) throws ParseException {
		CommandCLI cli = new CommandCLI(Sniffer.NAME, args);
	}
}
