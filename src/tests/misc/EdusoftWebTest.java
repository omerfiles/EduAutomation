package tests.misc;

import java.util.List;

import jsystem.framework.TestProperties;

import org.junit.After;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.openqa.selenium.logging.LogEntries;

import services.AudioService;
import services.PageHelperService;
import Enums.AutoParams;
import Enums.Browsers;
//import drivers.AndroidWebDriver;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	protected GenericWebDriver webDriver;
	public PageHelperService pageHelper;
	public AudioService audioService;

	String browser = null;

	@Override
	public void setup() throws Exception {
		super.setup();
		enableLoggin = true;
		// check if maven command line has a browser
		// browser=System.getProperty("browserCMD");
		// if(browser!=null){
		// System.out.println("Got borwser from maven cmd: "+browser);
		// }
		// System.out.println("remote machine: "
		// + System.getProperty("remote.machine"));
		// if (browser == null) {
		// browser = System.getProperty("browser");// getting browser name from
		// // pom.xml when running frm
		// // Jenkins
		// }
		// System.out
		// .println("browser name loaded from maven profile: " + browser);
		// if (browser == null) {
		// browser = configuration.getProperty("browser");// getting browser
		// // name from
		// properties file
		// }

		browser = configuration.getAutomationParam("browser", "browserCMD");

		if (browser.equals(Browsers.chrome.toString())) {
			webDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		} else if (browser.equals(Browsers.safari.toString())) {
			webDriver = (SafariWebDriver) ctx.getBean("SafariWebDriver");
		} else if (browser.equals(Browsers.IE.toString())) {
			webDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		} else if (browser.equals(Browsers.firefox.toString())) {
			webDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		}

		// else if (browser.equals(Browsers.android.toString())) {
		// webDriver = (AndroidWebDriver) ctx.getBean("AndroidWebDriver");
		// }
		if (webDriver == null) {
			// Assert.fail("No webdriver found. Please check properties file or pom for webdriver name");
			testResultService
					.addFailTest("No webdriver found. Please check properties file or pom for webdriver name");
		}
		webDriver.setReporter(report);

		// if
		// (webDriver.checNodeIsON(configuration.getProperty("remote.machine")))
		// {
		if (enableLoggin == true) {
			webDriver.setEnableConsoleLog(true);
		}
		webDriver.init(testResultService);
		try {
			// webDriver.maximize();
		} catch (Exception e) {
			System.out.println(e.toString());
			Assert.fail("openening Webdriver failed. Check that selenium node/grid are running and also check configurations");
		}
		// } else {
		// Assert.fail("Selenium grid node is off");
		// }

		pageHelper = (PageHelperService) ctx.getBean("PageHelperService");
		pageHelper.init(webDriver, autoInstitution, testResultService);
		audioService = (AudioService) ctx.getBean("AudioService");

		setEnableLoggin(true);

	}

	@After
	public void tearDown() throws Exception {

		// if (pageHelper.isLogoutNeeded()) {
		// pageHelper.logOut();
		// }

//		if (enableLoggin == true && browser.equals(Browsers.chrome.toString())) {
//			LogEntries logEntries = webDriver.getConsoleLogEntries();
//			boolean useLogFilter=true;
//			if(logFilter==null){
//				useLogFilter=false;
//			}
//			List<String[]> logList = textService.getListFromLogEntries(
//					logEntries, logFilter,useLogFilter);
//			String consoleLogPath = "files/consoleOutput/consoleLog"
//					+ dbService.sig() + ".csv";
//			textService.writeArrayistToCSVFile(consoleLogPath, logList);
//			System.out
//					.println("Console log can be found in: " + consoleLogPath);
//
//		}

		System.out.println("Start of EdusoftWebTest teardown");
		try {
			if (testResultService.hasFailedResults()) {

				webDriver.printScreen("FailCause_", null);
			}

			// if (pageHelper.isLogoutNeeded()) {
			// pageHelper.logOut();
			// }
		} catch (Exception e) {
			System.out.println("Exception in WebTest teardown");
			e.printStackTrace();
		} finally {
			webDriver.quitBrowser();
		}
		// screenRecorder.stop();
		System.out.println("end of EdusoftWebTest teardown");
		super.tearDown();
	}

	public String getSutAndSubDomain() {
		return configuration.getAutomationParam(AutoParams.sutUrl.toString(),
				AutoParams.sutUrl.toString() + "CMD")
				+ "//"
				+ configuration.getProperty("institutaion.subdomain");

	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public boolean isEnableLoggin() {
		return enableLoggin;
	}

	public String getLogFilter() {
		return logFilter;
	}

}
