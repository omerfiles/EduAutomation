package pageObjects.edo;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;
import tests.edo.newux.BasicNewUxTest;

public class NewUXLoginPage extends GenericPage {

	private static final String LOGOUT_XPATH = "//button[@ng-click='logout()']";
	private static final String SUBMIT = "submit1";

	public NewUXLoginPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	private String userNameTextbox = "userName";
	private String passwordTextbox = "password";

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

	public void enterUserName(String userName) throws Exception {
		clearUserName();
		webDriver.waitForElement(userNameTextbox, ByTypes.name).sendKeys(
				userName);
		webDriver.sendKey(Keys.TAB);
	}

	public void enterPassowrd(String password) throws Exception {
		clearPassword();
		webDriver.waitForElement(passwordTextbox, ByTypes.name).sendKeys(
				password);
	}

	public void clickOnSubmit() throws Exception {
		webDriver.waitForElement(SUBMIT, ByTypes.id).click();
	}

	public void clearUserName() throws Exception {
		webDriver.waitForElement(userNameTextbox, ByTypes.name).clear();

	}

	public void clearPassword() throws Exception {
		webDriver.waitForElement(passwordTextbox, ByTypes.name).clear();
	}

	public boolean isSubmitButtonEnabled() throws Exception {

//		boolean result = true;
		try {

			WebElement element = webDriver.waitForElement(SUBMIT, ByTypes.id,
					webDriver.getTimeout(), false);
			if (element.getAttribute("disabled").equals("disabled")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Submit button not enabled", false,
					true);
		}
		return false;
	}

	public boolean isLogOutDisplayed() throws Exception {
		WebElement webElement = webDriver.waitForElement(LOGOUT_XPATH,
				ByTypes.xpath);
		return webElement.isDisplayed();

	}

	public void clickOnLogout() throws Exception {
		webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath).click();
	}

	public NewUxHomePage loginAsStudent() throws Exception {
		return loginAsStudent(configuration.getProperty("student"),
				configuration.getProperty("student.user.password"));
	}

	public NewUxHomePage loginAsStudent(String userName, String password)
			throws Exception {
		enterUserName(userName);
		enterPassowrd(password);
		clickOnSubmit();

		return new NewUxHomePage(webDriver, testResultService);
	}

	// public String getUserNameErrorMessage()throws Exception{
	//
	// }
	// public String getPasswordErrorMessage()throws Exception{
	//
	// }

}
