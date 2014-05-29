package pageObjects.tms;

import Objects.UserObject;
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
	
	
	public GenericPage Login(UserObject userObject)throws Exception{
		webDriver.waitForElement("userName", "name").sendKeys(userObject.getUserName());
		webDriver.waitForElement("password", "name").sendKeys(userObject.getPassword());
		webDriver.waitForElement("//input[@value='login']", "xpath").click();
		return new TmsHomePage(webDriver);
		
	}
	



}
