package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class TeacherDetailsPage extends GenericPage {

	String mainWin=null;
	
	public TeacherDetailsPage(GenericWebDriver webDriver,TestResultService testResultService) throws Exception {
		super(webDriver ,testResultService);
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
		webDriver.waitForElement("FirstName", ByTypes.id).sendKeys(name);
		return this;
	}
	public TeacherDetailsPage typeTeacherLastName(String lastName)throws Exception{
		webDriver.waitForElement("LastName", ByTypes.id).sendKeys(lastName);
		return this;
	}
	public TeacherDetailsPage typeTeacherUserName(String UserName)throws Exception{
		webDriver.waitForElement("UserName", ByTypes.id).sendKeys(UserName);
		return this;
	}
	public TeacherDetailsPage typeTeacherPassword(String password)throws Exception{
		webDriver.waitForElement("Password", ByTypes.id).sendKeys(password);
		return this;
	}
	public TeacherDetailsPage addClass()throws Exception{
		webDriver.waitForElementAndClick("//select[@id='listLeft']//option[1]" , ByTypes.xpath);
		webDriver.waitForElementAndClick("Submit", ByTypes.id);
		return this;
	}
	public TmsHomePage clickOnSubmit()throws Exception{
		webDriver.switchToMainWindow(mainWin);
		webDriver.waitForElementAndClick("Submitbutton", ByTypes.name);
		return new TmsHomePage(webDriver,testResultService);
	}
	

}
