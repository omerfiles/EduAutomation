package pageObjects.tms;

import drivers.GenericWebDriver;
import pageObjects.GenericPage;

public class TeacherDetailsPage extends GenericPage {

	String mainWin=null;
	
	public TeacherDetailsPage(GenericWebDriver webDriver) throws Exception {
		super(webDriver);
	mainWin=	webDriver.switchToFrame("FormFrame");
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TeacherDetailsPage typeTeacherFirstName(String name)throws Exception{
		webDriver.waitForElement("FirstName", "id").sendKeys(name);
		return this;
	}
	public TeacherDetailsPage typeTeacherLastName(String lastName)throws Exception{
		webDriver.waitForElement("LastName", "id").sendKeys(lastName);
		return this;
	}
	public TeacherDetailsPage typeTeacherUserName(String UserName)throws Exception{
		webDriver.waitForElement("UserName", "id").sendKeys(UserName);
		return this;
	}
	public TeacherDetailsPage typeTeacherPassword(String password)throws Exception{
		webDriver.waitForElement("Password", "id").sendKeys(password);
		return this;
	}
	public TeacherDetailsPage addClass()throws Exception{
		webDriver.waitForElementAndClick("//select[@id='listLeft']//option[1]" , "xpath");
		webDriver.waitForElementAndClick("Submit", "id");
		return this;
	}
	public TmsHomePage clickOnSubmit()throws Exception{
		webDriver.switchToMainWindow(mainWin);
		webDriver.waitForElementAndClick("Submitbutton", "name");
		return new TmsHomePage(webDriver);
	}
	

}
