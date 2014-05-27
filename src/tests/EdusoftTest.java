package tests;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.Configuration;
import services.DbService;
import services.EraterService;
import services.NetService;
import services.TextService;
import drivers.GenericWebDriver;
import junit.framework.SystemTestCase4;
import junit.framework.TestCase;

public class EdusoftTest extends SystemTestCase4 {
	
	GenericWebDriver webDriver;
	protected TextService textService;
	Configuration configuration;
	DbService dbService;
	NetService netService;
	EraterService  eraterService;
	public ClassPathXmlApplicationContext ctx;
	
	@Before
	public void setup() throws Exception{
//		System.setProperty("java.ibrary.path","C:\\Users\\omers\\Downloads\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86");
		ctx=new ClassPathXmlApplicationContext("beans.xml");
		configuration=(Configuration)ctx.getBean("configuration");
		webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");
		
		textService=(TextService)ctx.getBean("TextSerivce");
		dbService=(DbService)ctx.getBean("DbService");
		netService=(NetService)ctx.getBean("NetService");
		eraterService=(EraterService)ctx.getBean("EraterService");
		
		
		webDriver.init(configuration.getProperty("remote.machine"), null);
		
		
//		dbService.dbConnect(configuration.getProperty("db.connection"), configuration.getProperty("db.connection.username"), configuration.getProperty("db.connection.password"));

		
		
	}
	
	@After
	public void tearDown()throws Exception{
		if(this.isPass()!=true){
			webDriver.printScreen("Test failed", null);
			
		}
		webDriver.closeBrowser();
//		dbService.closeConnection();
	}

}
