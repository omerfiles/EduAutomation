package tests;

import drivers.GenericWebDriver;

public class EdusoftWebTest extends EdusoftBasicTest {

	@Override
	public void setup() throws Exception {
		super.setup();
		webDriver = (GenericWebDriver) ctx.getBean("GenericWebDriver");
		webDriver.init(config.getProperty("remote.machine"), null);
	}

	@Override
	public void tearDown() throws Exception {
		if (this.isPass() != true) {
			webDriver.printScreen("Test failed", null);

		}
		webDriver.closeBrowser();
	}

	public String getSutAndSubDomain() {
		return config.getProperty("sut.url") + "//"
				+ config.getProperty("institutaion.subdomain");

	}
}
