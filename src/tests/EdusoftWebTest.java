package tests;

import org.junit.After;

import drivers.ChromeWebDriver;
import drivers.FirefoxDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	protected FirefoxDriver firefoxDriver;
	protected IEWebDriver ieWebDriver;
	protected ChromeWebDriver chromeWebDriver;

	@Override
	public void setup() throws Exception {
		super.setup();
		// webDriver = (GenericWebDriver) ctx.getBean("GenericWebDriver");
		// webDriver.init(config.getProperty("remote.machine"), null);
		firefoxDriver = (FirefoxDriver) ctx.getBean("FirefoxDriver");
		// firefoxDriver.init(config.getProperty("remote.machine"), null);
		ieWebDriver = (IEWebDriver) ctx.getBean("IEWebDriver");
		// ieWebDriver.init(config.getProperty("remote.machine"), null);
		chromeWebDriver = (ChromeWebDriver) ctx.getBean("ChromeWebDriver");
		// chromeWebDriver.init(config.getProperty("remote.machine"), null);
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
		}

		chromeWebDriver.closeBrowser();
		firefoxDriver.closeBrowser();
		ieWebDriver.closeBrowser();
		super.tearDown();
	}

	public String getSutAndSubDomain() {
		return config.getProperty("sut.url") + "//"
				+ config.getProperty("institutaion.subdomain");

	}
}
