package pageObjects.tms;

import Enums.ByTypes;
import Objects.UserObject;
import drivers.GenericWebDriver;
import pageObjects.EdoLoginPage;
import pageObjects.GenericPage;
import pageObjects.LoginPage;
import services.Configuration;

public class TmsLoginPage extends LoginPage {

	public TmsLoginPage(GenericWebDriver webDriver) {
		super(webDriver);
		
		// TODO Auto-generated constructor stub
	}
	public TmsLoginPage OpenPage(String url) throws Exception {
		webDriver.openUrl(url);
		return this;
	}
	
	
	public TmsHomePage Login(UserObject userObject)throws Exception{
		webDriver.waitForElement("userName",ByTypes.name).sendKeys(userObject.getUserName());
		webDriver.waitForElement("password", ByTypes.name).sendKeys(userObject.getPassword());
		webDriver.waitForElement("//input[@value='Login']",ByTypes.xpath).click();
		return new TmsHomePage(webDriver);
		
	}
	



}
