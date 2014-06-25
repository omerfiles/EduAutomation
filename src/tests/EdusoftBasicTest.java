package tests;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.Configuration;
import services.DbService;
import services.EraterService;
import services.NetService;
import services.TextService;
import drivers.GenericWebDriver;
import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.SystemTestCase4;

public class EdusoftBasicTest extends SystemTestCase4 {
	
	

	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration config;
	protected DbService dbService;
	NetService netService;
	protected EraterService eraterService;
	public ClassPathXmlApplicationContext ctx;
	
	protected boolean inStep=false;
	protected String testCaseId=null;

	
	
	
	@Before
	public void setup() throws Exception {
		// System.setProperty("java.ibrary.path","C:\\Users\\omers\\Downloads\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86");
		
		
		
//		ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		ctx = new ClassPathXmlApplicationContext("beans.xml");
		config = (Configuration) ctx.getBean("configuration");
		// webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");

		textService = (TextService) ctx.getBean("TextSerivce");
		dbService = (DbService) ctx.getBean("DbService");
		netService = (NetService) ctx.getBean("NetService");
		eraterService = (EraterService) ctx.getBean("EraterService");

		// webDriver.init(config.getProperty("remote.machine"), null);

		// dbService.dbConnect(configuration.getProperty("db.connection"),
		// configuration.getProperty("db.connection.username"),
		// configuration.getProperty("db.connection.password"));
		

	}

	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}
	

	@After
	public void tearDown() throws Exception {
		
	}
	
	public void startStep(String stepName) throws Exception{
		if(inStep==true){
			report.stopLevel();
		}
//		report.step(stepName);
		report.startLevel(stepName, EnumReportLevel.CurrentPlace);
		inStep=true;
	}
	public void endStep()throws Exception{
		report.stopLevel();
	}
	

}
