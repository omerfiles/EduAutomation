package tests;

import org.junit.After;
import org.openqa.selenium.safari.SafariDriver;

import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import drivers.SafariWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	protected FirefoxWebDriver firefoxDriver;
	protected IEWebDriver ieWebDriver;
	protected ChromeWebDriver chromeWebDriver;
	protected SafariWebDriver  safariDriver;

	@Override
	public void setup() throws Exception {
		super.setup();
		firefoxDriver = (FirefoxWebDriver) ctx.getBean("FirefoxWebDriver");
		ieWebDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		chromeWebDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		safariDriver=(SafariWebDriver)ctx.getBean("SafariWebDriver");
	}

	@After
	public void tearDown() throws Exception {

		if (this.isPass == false) {
			if (chromeWebDriver.isInitialized() == true) {
				chromeWebDriver.printScreen(this.getMethodName(),null);
			} else if (ieWebDriver.isInitialized() == true) {
				ieWebDriver.printScreen(this.getMethodName(),null);
			} else if (firefoxDriver.isInitialized() == true) {
				firefoxDriver.printScreen(this.getMethodName(),null);
			}
			else if(safariDriver.isInitialized()==true){
				safariDriver.printScreen(this.getMethodName(),null);
			}
		}

		chromeWebDriver.closeBrowser();
		firefoxDriver.closeBrowser();
		ieWebDriver.closeBrowser();
		safariDriver.closeBrowser();
		super.tearDown();
	}

	public String getSutAndSubDomain() {
		return config.getProperty("sut.url") + "//"
				+ config.getProperty("institutaion.subdomain");

	}
}
