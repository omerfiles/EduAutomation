package services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jsystem.framework.report.Reporter.EnumReportLevel;

@Service
public class Reporter {
	ArrayList<String> reportLogs;

	public Reporter() {
		// TODO Auto-generated constructor stub
	}

	public void init() {
		reportLogs = new ArrayList<>();
	}

	public void report(String message){
		//append text to list
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
