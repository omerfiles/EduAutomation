package pageObjects.tms;

import drivers.GenericWebDriver;
import pageObjects.EdoLoginPage;
import pageObjects.GenericPage;
import pageObjects.LoginPage;
import services.Configuration;

public class tmsLoginPage extends LoginPage {

	public tmsLoginPage(GenericWebDriver webDriver) {
		super(webDriver);
		
		// TODO Auto-generated constructor stub
	}
	public tmsLoginPage OpenPage(String url) throws Exception {
		webDriver.openUrl(url);
		return this;
	}



}
