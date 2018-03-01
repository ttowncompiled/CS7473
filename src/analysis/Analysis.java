package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Analysis {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		ARPAnalysis.analysis();
		DNSAnalysis.analysis();
		FTPFailAnalysis.analysis();
		FTPSuccAnalysis.analysis();
		HTTPAnalysis.analysis();
		TelnetAnalysis.analysis();
	}
}
