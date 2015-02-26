package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;

public class NewUXLoginPage extends GenericPage {

	public NewUXLoginPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}
	
	private String userNameTextbox="userName";
	private String passwordTextbox="password";

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
	
	public void enterUserName(String userName)throws Exception{
		clearUserName();
		webDriver.waitForElement(userNameTextbox, ByTypes.name).sendKeys(userName);
	}
	public void enterPassowrd(String password)throws Exception{
		clearPassword();
		webDriver.waitForElement(passwordTextbox, ByTypes.name).sendKeys(password );
	}
	public void clickOnSubmit()throws Exception{
		webDriver.waitForElement("submit", ByTypes.id).click();
	}

	public void clearUserName() throws Exception {
		webDriver.waitForElement(userNameTextbox, ByTypes.name).clear();
		
	}
	public void clearPassword()throws Exception{
		webDriver.waitForElement(passwordTextbox, ByTypes.name).clear();
	}
	
	public boolean isSubmitButtonEnabled()throws Exception{
		return webDriver.waitForElement("submit", ByTypes.id).isEnabled();
	}
	
	
//	public String getUserNameErrorMessage()throws Exception{
//		
//	}
//	public String getPasswordErrorMessage()throws Exception{
//		
//	}

}
