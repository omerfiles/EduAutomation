package tests.misc;

import java.lang.reflect.Method;

import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.SystemTestCase4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.AudioService;
import services.Configuration;
import services.DbService;
import services.EraterService;
import services.InstitutionService;
import services.NetService;
import services.TestResultService;
import services.TextService;
import Interfaces.TestCaseParams;
import Interfaces.TestCaseParams;
import Objects.AutoInstitution;
import drivers.GenericWebDriver;

public class EdusoftBasicTest extends SystemTestCase4 {

	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration configuration;
	protected DbService dbService;
	protected NetService netService;
	protected EraterService eraterService;
	protected InstitutionService institutionService;
	protected TestResultService testResultService;
	// protected static AudioService audioService;
	public ClassPathXmlApplicationContext ctx;

	protected boolean inStep = false;
	protected String testCaseId = null;

	protected AutoInstitution autoInstitution;

	private boolean printResults = true;

	@Before
	public void setup() throws Exception {
		System.out.println("url from maven command line: "
				+ System.getProperty("URL"));

		ctx = new ClassPathXmlApplicationContext("beans.xml");
		configuration = (Configuration) ctx.getBean("configuration");
		// webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");

		textService = (TextService) ctx.getBean("TextSerivce");
		dbService = (DbService) ctx.getBean("DbService");
		netService = (NetService) ctx.getBean("NetService");
		eraterService = (EraterService) ctx.getBean("EraterService");
		institutionService = (InstitutionService) ctx
				.getBean("InstitutionService");
		testResultService = (TestResultService) ctx
				.getBean("TestResultService");
		// audioService = (AudioService) ctx.getBean("AudioService");

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

		institutionService.init();
		autoInstitution = institutionService.getInstitution();
		System.out.println(autoInstitution.getInstitutionId());

		// System.out.println("Automation isntitution id is: "
		// + autoInstitution.getInstitutionId());
	}

	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}

	@After
	public void tearDown() throws Exception {
		// report.startLevel("Test case id is: " + this.testCaseId,
		// EnumReportLevel.MainFrame);
		System.out.println("testResultService:" + testResultService.toString());
		System.out.println("Test failed?: "
				+ testResultService.hasFailedResults());
		if (printResults == true && testResultService.hasFailedResults()) {
			testResultService.printAllFailures();
		}
		if (testResultService.hasFailedResults() && this.isPass == true) {
			Assert.fail("Test failed due to several errors");
		}
		

		// if (this.isPass == false) {
		// report.startLevel("Test failed", EnumReportLevel.MainFrame);
		// }
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

	public String getTestCaseId(String methodName, Class c) {
		Method method = null;
		try {
			method = c.getDeclaredMethod(methodName, null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Method[] methods = c.getMethods();
		// for (Method m : methods) {
		// if (m.isAnnotationPresent(TestCaseParams.class)) {
		// TestCaseParams ta = m.getAnnotation(TestCaseParams.class);
		// System.out.println("test cas_e id: " + ta.testCaseID());
		// testCaseId = ta.testCaseID();
		// break;
		// }
		// }

		if (method.isAnnotationPresent(TestCaseParams.class)) {
			TestCaseParams tc = method.getAnnotation(TestCaseParams.class);
			testCaseId = tc.testCaseID();
		}

		return testCaseId;
	}

	public boolean isPrintResults() {
		return printResults;
	}

	public void setPrintResults(boolean printResults) {
		this.printResults = printResults;
	}

}
