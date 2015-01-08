package services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jsystem.framework.report.Reporter.EnumReportLevel;

@Service("reporter")
public class Reporter {

	private static Reporter reporter = new Reporter();
	ArrayList<String> reportLogs = new ArrayList<>();

	public static Reporter getInstance() {

		return reporter;
	}

	// public void init() {
	// reportLogs = new ArrayList<>();
	// }

	public void report(String message) {
		// append text to list
		reportLogs.add(message);
	}

	public void startLevel(String string, EnumReportLevel currentplace) {
		reportLogs.add(string);

	}

	public void startLevel(String string) {
		reportLogs.add(string);

	}

	public void stopLevel() {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> getReportLogs() {
		return reportLogs;
	}

}
