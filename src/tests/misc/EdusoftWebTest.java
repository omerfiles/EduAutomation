package tests.misc;



import org.junit.After;
import org.junit.Assert;

import services.PageHelperService;
import Enums.Browsers;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	protected GenericWebDriver webDriver;
	public PageHelperService pageHelper;

	

	String browser = null;

	@Override
	public void setup() throws Exception {
		super.setup();
		System.out.println("remote machine: "
				+ System.getProperty("remote.machine"));
		if (browser == null) {
			browser = System.getProperty("browser");// getting browser name from
													// pom.xml when running frm
													// Jenkins
		}
		System.out
				.println("browser name loaded from maven profile: " + browser);
		if (browser == null) {
			browser = configuration.getProperty("browser");// getting browser
															// name from
			// properties file
		}
		if (browser.equals(Browsers.chrome.toString())) {
			webDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		} else if (browser.equals(Browsers.safari.toString())) {
			webDriver = (SafariWebDriver) ctx.getBean("SafariWebDriver");
		} else if (browser.equals(Browsers.IE.toString())) {
			webDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		} else if (browser.equals(Browsers.firefox.toString())) {
			webDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		}
		if (webDriver == null) {
			Assert.fail("No webdriver found. Please check properties file or pom for webdriver name");
		}

		webDriver.init();
		webDriver.maximize();
		pageHelper = (PageHelperService) ctx.getBean("PageHelperService");
		pageHelper.init(webDriver, autoInstitution);


	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Start of EdusoftWebTest teardown");
		try {
			if (this.isPass == false) {

				webDriver.printScreen(this.getMethodName(), null);
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
		return configuration.getProperty("sut.url") + "//"
				+ configuration.getProperty("institutaion.subdomain");

	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
}
