package tests.misc;

import java.util.concurrent.TimeUnit;

import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
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
import services.MailService;
import services.MultiBrowserRunner;
import services.NetService;
import services.StudentService;
import services.TestResultService;
import services.TextService;
import Enums.Browsers;
import Enums.TestRunnerType;
import Objects.AutoInstitution;
import Objects.GenericTestObject;
import drivers.AndroidWebDriver;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.HeadlessBrowser;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;

@RunWith(ExtendedRunner.class)
//@RunWith(MultiBrowserRunner.class)
// @ContextConfiguration(locations={ "applicationContext-test.xml"})
public class EdusoftBasicTest extends GenericTestObject {

	protected GenericWebDriver webDriver;
	protected HeadlessBrowser  headlessBrowser;

	protected Configuration configuration;

	protected EraterService eraterService;
	protected InstitutionService institutionService;
	public StudentService studentService;

	// protected services.Reporter report;
	protected MailService mailService;
	// protected static AudioService audioService;
	// public ClassPathXmlApplicationContext ctx;
	protected boolean enableLoggin = false;

	protected String logFilter = null;
	private int testTimeoutInMinutes = Integer.parseInt(System.getProperty("testTimeOut")) ;
//	private int testTimeoutInSeconds=20;
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

	String browser = null;

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

	@Rule
	public TestName testName = new TestName();

	// junit <4.12
	// @Rule
	// public Timeout timeout=new Timeout(300000);//5 minutes timeout

	
	
	@Rule
	public final TestRule testTimeOut = Timeout.builder()
	
			.withTimeout(testTimeoutInMinutes, TimeUnit.MINUTES)
			.withLookingForStuckThread(false).build();

	// public static Reporter report = ListenerstManager.getInstance();

	@Before
	public void setup() throws Exception {
		super.setup();
		
		testCaseId = System.getProperty("testCaseId");
		testCaseName = testName.getMethodName();
		// System.out.println("Test case name:" + testCaseName);
		// System.out.println("Test case is in edusoftBasicTest:" + testCaseId);

		System.out.println("url from maven command line: "
				+ System.getProperty("URL"));

		// ctx = new ClassPathXmlApplicationContext("beans.xml");
		configuration = (Configuration) ctx.getBean("configuration");
		// webDriver=(GenericWebDriver)ctx.getBean("GenericWebDriver");

		eraterService = (EraterService) ctx.getBean("EraterService");
		institutionService = (InstitutionService) ctx
				.getBean("InstitutionService");
		studentService = (StudentService) ctx.getBean("StudentService");

		// report = (services.Reporter) ctx.getBean("Reporter");
		mailService = (MailService) ctx.getBean("MailService");
		// report.init();
		netService = (NetService) ctx.getBean("NetService");
		
		headlessBrowser=(HeadlessBrowser)ctx.getBean("HeadlessBrowser");
		// audioService = (AudioService) ctx.getBean("AudioService");
		report.writelogger("Test name:" + testCaseName);
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
			String fileName = "testlog" + testCaseName + dbService.sig()
					+ ".csv";

			// if running in CI
			TestRunnerType runner = configuration.getTestRunner();
			if (runner == TestRunnerType.CI) {

				String path = "smb://10.1.0.111/automationLogs/" + fileName;
				textService.writeListToSmbFile(path, report.getReportLogs(),
						netService.getAuth());
			} else {
				String path = System.getProperty("user.dir") + "/log//current/"
						+ fileName;
				textService.writeListToCsvFile(path, report.getReportLogs());

			}

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
		this.testTimeoutInMinutes = testTimeoutInSeconds;
	}
	
	

	public void selectBrowser() {

		if (System.getProperty("browserParam") == null
				|| System.getProperty("browserParam").equals(
						Browsers.empty.toString())) {

			browser = configuration.getAutomationParam("browser", "browserCMD");
		} else {
			browser = System.getProperty("browserParam");
		}

		if (browser.equals(Browsers.chrome.toString())) {
			webDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		} else if (browser.equals(Browsers.safari.toString())) {
			webDriver = (SafariWebDriver) ctx.getBean("SafariWebDriver");
		} else if (browser.equals(Browsers.IE.toString())) {
			webDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		} else if (browser.equals(Browsers.firefox.toString())) {
			webDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		} else if (browser.equals(Browsers.android.toString())) {
			webDriver = (AndroidWebDriver) ctx.getBean("AndroidWebDriver");
		}
	}

}
