package tests;

import jsystem.framework.report.Reporter.EnumReportLevel;

import org.junit.After;
import org.openqa.selenium.safari.SafariDriver;

import services.PageHelperService;
import Enums.Browsers;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

//	protected FirefoxWebDriver firefoxDriver;
//	protected IEWebDriver ieWebDriver;
//	protected ChromeWebDriver chromeWebDriver;
//	protected SafariWebDriver safariDriver;
	
	protected GenericWebDriver webDriver;
	protected PageHelperService pageHelper;

	String browser = null;

	@Override
	public void setup() throws Exception {
		super.setup();
		System.out.println("remote machine: "+ System.getProperty("remote.machine"));
		browser = System.getProperty("browser");// getting browser name from
												// pom.xml when running frm
												// Jenkins
		System.out.println("browser name loaded from maven profile: "+browser);
		if (browser == null) {
			browser = config.getProperty("browser");// getting browser name from
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
		
		webDriver.init();
		webDriver.maximize();
//		webDriver.maximize();
		pageHelper=(PageHelperService)ctx.getBean("PageHelperService");
		pageHelper.init(webDriver);

	}

	@After
	public void tearDown() throws Exception {

//		if (this.isPass == false) {
//			if (chromeWebDriver.isInitialized() == true) {
//				chromeWebDriver.printScreen(this.getMethodName(), null);
//			} else if (ieWebDriver.isInitialized() == true) {
//				ieWebDriver.printScreen(this.getMethodName(), null);
//			} else if (firefoxDriver.isInitialized() == true) {
//				firefoxDriver.printScreen(this.getMethodName(), null);
//			} else if (safariDriver.isInitialized() == true) {
//				safariDriver.printScreen(this.getMethodName(), null);
//			}
//		}
//
//		chromeWebDriver.quitBrowser();
//		firefoxDriver.quitBrowser();
//		ieWebDriver.quitBrowser();
//		safariDriver.quitBrowser();
		if (this.isPass == false) {
			
			webDriver.printScreen(this.getMethodName(), null);
		}
		webDriver.quitBrowser();
		
		super.tearDown();
	}

	public String getSutAndSubDomain() {
		return config.getProperty("sut.url") + "//"
				+ config.getProperty("institutaion.subdomain");

	}
}
