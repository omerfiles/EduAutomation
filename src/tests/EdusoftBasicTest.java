package tests;

import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.SystemTestCase4;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.AudioService;
import services.Configuration;
import services.DbService;
import services.EraterService;
import services.InstitutionService;
import services.NetService;
import services.TextService;
import Objects.AutoInstitution;
import drivers.GenericWebDriver;

public class EdusoftBasicTest extends SystemTestCase4 {

	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration config;
	protected DbService dbService;
	NetService netService;
	protected EraterService eraterService;
	protected InstitutionService institutionService;
	protected static AudioService audioService;
	public ClassPathXmlApplicationContext ctx;

	protected boolean inStep = false;
	protected String testCaseId = null;

	protected AutoInstitution autoInstitution;

	@Before
	public void setup() throws Exception {
		// System.setProperty("java.ibrary.path","C:\\Users\\omers\\Downloads\\Microsoft JDBC Driver 4.0 for SQL Server\\sqljdbc_4.0\\enu\\auth\\x86");

		// ctx = new ClassPathXmlApplicationContext("beans.xml");

		ctx = new ClassPathXmlApplicationContext("beans.xml");
		config = (Configuration) ctx.getBean("configuration");
		// webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");

		textService = (TextService) ctx.getBean("TextSerivce");
		dbService = (DbService) ctx.getBean("DbService");
		netService = (NetService) ctx.getBean("NetService");
		eraterService = (EraterService) ctx.getBean("EraterService");
		institutionService = (InstitutionService) ctx
				.getBean("InstitutionService");
		audioService = (AudioService) ctx.getBean("AudioService");

		int institutionId;
		if (System.getProperty("institutionId") == null) {
			institutionId = 1;
			// autoInstitution =
			// institutionService.getAutoInstitutions().get(0);
		} else {
			// autoInstitution = institutionService.getAutoInstitutions().get(
			// Integer.parseInt(System.getProperty("institutionId")));
			institutionId = Integer.parseInt(System
					.getProperty("institutionId"));
		}

		institutionService.init(institutionId);
		autoInstitution = institutionService.getInstitution();

		// System.out.println("Automation isntitution id is: "
		// + autoInstitution.getInstitutionId());
	}

	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}

	@After
	public void tearDown() throws Exception {
		report.startLevel("Test case id is: " + this.testCaseId,
				EnumReportLevel.MainFrame);
		if (this.isPass == false) {
			report.startLevel("Test failed", EnumReportLevel.MainFrame);
		}
	}

	public void startStep(String stepName) throws Exception {
		if (inStep == true) {
			report.stopLevel();
		}
		// report.step(stepName);
		report.startLevel(stepName, EnumReportLevel.CurrentPlace);
		inStep = true;
	}

	public void endStep() throws Exception {
		report.stopLevel();
	}

}
