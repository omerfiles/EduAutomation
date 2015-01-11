package tests.misc;

import java.util.concurrent.TimeUnit;

import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.Configuration;
import services.DbService;
import services.EraterService;
import services.ExtendedRunner;
import services.InstitutionService;
import services.NetService;
import services.TestResultService;
import services.TextService;
import Objects.AutoInstitution;
import drivers.GenericWebDriver;

@RunWith(ExtendedRunner.class)
// @ContextConfiguration(locations={ "applicationContext-test.xml"})
public class EdusoftBasicTest extends TestCase {

	protected GenericWebDriver webDriver;

	protected TextService textService;
	protected Configuration configuration;
	protected DbService dbService;
	protected NetService netService;
	protected EraterService eraterService;
	protected InstitutionService institutionService;
	protected TestResultService testResultService;
	protected services.Reporter report;
	// protected static AudioService audioService;
	public ClassPathXmlApplicationContext ctx;
	protected boolean enableLoggin = false;

	protected String logFilter = null;
	private int testTimeoutInSeconds = 3000;

	protected boolean inStep = false;
	protected String testCaseId = null;
	public String testStatus = null;
	boolean testHasFailedResult;

	protected AutoInstitution autoInstitution;

	private boolean printResults = true;
	// public services.Reporter report=new services.Reporter();

	private boolean hasFailures;
	private String testCaseId1;
	private String testCaseName;

	@Rule
	public TestWatcher watcher = new TestWatcher() {

		@Override
		protected void succeeded(Description description) {
			// TODO Auto-generated method stub
			testStatus = "passed";
			testHasFailedResult = testResultService.hasFailedResults();
			if (testHasFailedResult == true) {
				fail();
				if (printResults == true) {
					testResultService.printAllFailures();
				}
			}

			System.out.println(testHasFailedResult);
			super.succeeded(description);
		}

		@Override
		protected void failed(Throwable e, Description description) {
			// TODO Auto-generated method stub
			testStatus = "failed";
			if (testHasFailedResult == true && printResults == true) {

				testResultService.printAllFailures();

			}
			super.failed(e, description);
		}

	};
	// junit <4.12
	// @Rule
	// public Timeout timeout=new Timeout(300000);//5 minutes timeout

	@Rule
	public final TestRule testTimeOut = Timeout.builder()
			.withTimeout(testTimeoutInSeconds, TimeUnit.SECONDS)
			.withLookingForStuckThread(true).build();

	// public static Reporter report = ListenerstManager.getInstance();

	@Before
	public void setup() throws Exception {

		testCaseId = System.getProperty("testCaseId");
		testCaseName=System.getProperty("testCaseName");
		System.out.println("Test case name:"+ testCaseName);
		System.out.println("Test case is in edusoftBasicTest:" + testCaseId);

		System.out.println("url from maven command line: "
				+ System.getProperty("URL"));

		ctx = new ClassPathXmlApplicationContext("beans.xml");
		configuration = (Configuration) ctx.getBean("configuration");
		// webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");

		textService = (TextService) ctx.getBean("TextSerivce");
		dbService = (DbService) ctx.getBean("DbService");
		
		eraterService = (EraterService) ctx.getBean("EraterService");
		institutionService = (InstitutionService) ctx
				.getBean("InstitutionService");
		testResultService = (TestResultService) ctx
				.getBean("testResultService");
		report = (services.Reporter) ctx.getBean("reporter");
//		report.init();
		netService = (NetService) ctx.getBean("NetService");
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


	}

	public void sleep(int seconds) throws Exception {
		report.report("Sleeping for " + seconds + "seconds");
		Thread.sleep(seconds * 1000);
	}

	@After
	public void tearDown() throws Exception {

		try {

			String fileName = "testlog"+testCaseName + dbService.sig() + ".csv";
			String path = "smb://10.1.0.83/automationLogs/" + fileName;
			textService.writeListToSmbFile(path, report.getReportLogs(),
					netService.getAuth());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean testHasFailedResult = testResultService.hasFailedResults();
		if (printResults == true && testHasFailedResult) {
			testResultService.printAllFailures();
		}

		System.out.println("test passed?: " + testStatus);
		if (testHasFailedResult) {
			System.out.println("Test failed due to some errors");
			// Assert.fail("Test failed due to some errors");
		}

	}

	public void startStep(String stepName) throws Exception {
		if (inStep == true) {
			report.stopLevel();
		}
		// report.step(stepName);
		report.startLevel(stepName, EnumReportLevel.CurrentPlace);
		// System.out.println("Step: " + stepName);
		inStep = true;
	}

	public void printMessage(String message) {

		System.out.println(message);
	}

	public void endStep() throws Exception {
		report.stopLevel();
	}



	public boolean isPrintResults() {
		return printResults;
	}

	public void setPrintResults(boolean printResults) {
		this.printResults = printResults;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public void setEnableLoggin(boolean enableLoggin) {
		this.enableLoggin = enableLoggin;
	}

	public void setLogFilter(String logFilter) {
		this.logFilter = logFilter;
	}

	public void setTestTimeoutInSeconds(int testTimeoutInSeconds) {
		this.testTimeoutInSeconds = testTimeoutInSeconds;
	}

}
