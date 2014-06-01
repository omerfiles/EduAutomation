package tests;

import org.junit.After;

import drivers.ChromeWebDriver;
import drivers.FirefoxDriver;
import drivers.GenericWebDriver;
import drivers.IEWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	FirefoxDriver firefoxDriver;
	IEWebDriver ieWebDriver;
	ChromeWebDriver chromeWebDriver;

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
