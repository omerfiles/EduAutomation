package services;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Service;

import jsystem.framework.report.Reporter.EnumReportLevel;

@Service("reporter")
public class Reporter {

	private static Reporter reporter = new Reporter();
	ArrayList<String> reportLogs = new ArrayList<>();

	static final Logger logger = Logger.getLogger(Reporter.class);

	public static Reporter getInstance() throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		initLogger();

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

	public void writelogger(String text) {

		logger.info(text);
		

	}

	public static void initLogger() throws IOException {
		// HTMLLayout htmlLayout=new HTMLLayout();
		Objects.Logging.HtmlLogLayout htmlLayout = new Objects.Logging.HtmlLogLayout();
		
		String pattern = "%r [%t] %-5p %c %x - %m%n";

		// SimpleLayout layout = new SimpleLayout();
		FileAppender appender = new FileAppender(htmlLayout,
				System.getProperty("user.dir") + "/log//current//log.html");

		// LogAppender appender =new LogAppender();
//		appender.setLayout(new HTMLLayout());
//		appender.setLayout(layout);
		logger.addAppender(appender);
	}

}
