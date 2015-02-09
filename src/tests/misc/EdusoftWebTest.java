package tests.misc;

import java.util.List;

import jsystem.framework.TestProperties;

import org.junit.After;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.openqa.selenium.logging.LogEntries;

import services.AudioService;
import services.PageHelperService;
import services.StudentService;
import Enums.AutoParams;
import Enums.Browsers;
import drivers.AndroidWebDriver;
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

		browser = configuration.getAutomationParam("browser", "browserCMD");

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

		if (webDriver == null) {
			testResultService
					.addFailTest("No webdriver found. Please check properties file or pom for webdriver name");
		}
		// webDriver.setReporter(report);

		if (enableLoggin == true) {
			webDriver.setEnableConsoleLog(true);
		}
		webDriver.init();
		String timeout = configuration.getAutomationParam(AutoParams.timeout.toString(),
				"timeOutCMD");
		report.report("Default timeout was set to: "+timeout);
		webDriver.setTimeout(Integer.valueOf(timeout));
		webDriver.maximize();
		try {

		} catch (Exception e) {
			System.out.println(e.toString());
			Assert.fail("openening Webdriver failed. Check that selenium node/grid are running and also check configurations");
		}

		pageHelper = (PageHelperService) ctx.getBean("PageHelperService");
		pageHelper.init(webDriver, autoInstitution, testResultService);
		audioService = (AudioService) ctx.getBean("AudioService");
		

		setEnableLoggin(true);
		testResultService.setWebDriver(webDriver);

	}

	@After
	public void tearDown() throws Exception {

		System.out.println("Start of EdusoftWebTest teardown");
		try {

			// print console log if browser was chrome
			if (browser.equals(Browsers.chrome.toString())) {
//				webDriver.printConsoleLogs("", false);
			}

			if (testResultService.hasFailedResults()) {

				webDriver.printScreen("FailCause_", null);
			}

		} catch (Exception e) {
			System.out.println("Exception in WebTest teardown");
			e.printStackTrace();
		} finally {
			webDriver.quitBrowser();
		}
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
