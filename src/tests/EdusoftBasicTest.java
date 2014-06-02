package tests;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.Configuration;
import services.DbService;
import services.EraterService;
import services.NetService;
import services.TextService;
import drivers.FirefoxDriver;
import drivers.GenericWebDriver;
import junit.framework.SystemTestCase4;
import junit.framework.TestCase;

public class EdusoftBasicTest extends SystemTestCase4 {

	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration config;
	protected DbService dbService;
	NetService netService;
	protected EraterService eraterService;
	public ClassPathXmlApplicationContext ctx;

	@Before
	public void setup() throws Exception {
		// System.setProperty("java.ibrary.path","C:\\Users\\omers\\Downloads\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86");
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

		if (this.isPass() != true) {
			webDriver.printScreen("Test failed", null);

		}
	}

}
