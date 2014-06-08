package tests;

import jsystem.extensions.report.junit.JUnitReporter;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.Configuration;
import services.DbService;
import services.EraterService;
import services.NetService;
import services.TextService;

import com.aqua.anttask.jsystem.JUnitTest;

import drivers.GenericWebDriver;

public class JunitBasicTest  {
	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration config;
	protected DbService dbService;
	NetService netService;
	protected EraterService eraterService;
	public ClassPathXmlApplicationContext ctx;
	public JUnitReporter jUnitReporter;
	
	@Before
	public void setup() throws Exception {
		// System.setProperty("java.ibrary.path","C:\\Users\\omers\\Downloads\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86");
		jUnitReporter=new JUnitReporter();
		
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
		jUnitReporter.report("title", "message", true, false);
		Thread.sleep(seconds * 1000);
	}

	@After
	public void tearDown() throws Exception {

		
	}


}
