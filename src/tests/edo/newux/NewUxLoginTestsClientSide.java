package tests.edo.newux;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import pageObjects.edo.NewUXLoginPage;
import tests.misc.EdusoftWebTest;

public class NewUxLoginTestsClientSide extends BasicNewUxTest {

	NewUXLoginPage loginPage;

	private final String userNameLessThen5Chars = "Your name is required to be at least 5 characters";
	private final String userNameMoreThen15Chars = "Your name cannot be longer than 15 characters";
	private final String passCharsAndNumbersOnly = "Please use only letters and/or numbers in your password.";

	private final String usernameCharsAndNumbersOnly = "Please use only letters and/or numbers in your user name.";
	private final String passLessThen5chars = "Your password must have at least 5 characters.";
	private final String passMoreThen15chars = "";

	@Before
	public void setup() throws Exception {
		super.setup();
		// loginPage = pageHelper.openCILatestLoginPage();
	}

	@Test
	@TestCaseParams(testCaseID = { "18278" })
	public void loginValidationFields() throws Exception {

		report.report("Test Blank user name");
		// submit button is disabled when username or password are blank
		// validateThatNoUserNameErrorMessageIsDIsplayed();
		// System.out.println("Submit button enabled? "
		// + loginPage.isSubmitButtonEnabled());

		loginPage = new NewUXLoginPage(webDriver, testResultService);

		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(), "Submit button was enabled");

		// report.report("User name with less then 5 chars");
		// loginPage.enterUserName("aaaa");
		// validateThatNoUserNameErrorMessageIsDIsplayed();

		// report.report("User name is more then 15 chars");
		// // validateUserNameErrorMessage(userNameMoreThen15Chars);
		//
		// loginPage.enterUserName("aaaaaaaaaaaaaaaa");
		// validateErrorMessage(userNameMoreThen15Chars);
		// // validateThatNoUserNameErrorMessageIsDIsplayed();

		report.report("Special char in user name");
		loginPage.enterUserName("aaaaaaaa#");
		validateErrorMessage(usernameCharsAndNumbersOnly,
				"Special char in user name", "error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(), "Submit button was enabled");

		report.report("No english chars");
		loginPage.enterUserName("aaaañzzz");
		validateErrorMessage(usernameCharsAndNumbersOnly, "No english chars",
				"error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(), "Submit button was enabled");

		report.report("Empty password");

		// report.report("Password is less then 5 chars");
		// loginPage.enterPassowrd("1234");
		// validateErrorMessage(passLessThen5chars);

		// report.report("Password is more then  15 chars");
		// loginPage.enterPassowrd("123456789azzzzz");
		// validateErrorMessage(passMoreThen15chars);

		report.report("Password with special chars");
		loginPage.enterPassowrd("123$4444");
		validateErrorMessage(passCharsAndNumbersOnly,
				"Password with special chars",
				"loginForm.password.$error.pattern");
		testResultService.assertEquals(false,
				loginPage.isSubmitButtonEnabled(), "Submit button was enabled");

		report.report("Password with hebrew chars");

		report.report("Wrong password");

		report.report("Wrong username");

		report.report("Valid user name and password");
		loginPage.enterUserName(configuration.getStudentUserName());
		loginPage.enterPassowrd("12345");
		testResultService.assertEquals(true, loginPage.isSubmitButtonEnabled(),
				"Submit button was enabled");
		loginPage.clickOnSubmit();
		testResultService.assertEquals(true, loginPage.isLogOutDisplayed(),
				"Log out is not displayed");

	}

	private void validateThatNoUserNameErrorMessageIsDIsplayed()
			throws Exception {
		WebElement errorElement = webDriver.waitForElement(
				"//div[@class='error'][contains(@ng-show,'userName')]",
				ByTypes.xpath, webDriver.getTimeout(), false);
		List<WebElement> classErrors = webDriver.getChildElementsByXpath(
				errorElement, "//small");
		for (int i = 0; i < classErrors.size(); i++) {
			if (classErrors.get(i).isDisplayed()) {
				testResultService
						.addFailTest("Error text appeared when it should not. text was: "
								+ classErrors.get(i).getText());
			}
		}
		webDriver.printScreen("During validation");

	}

	public void validateErrorMessage(String messageText,
			String validatationStage, String errorType) throws Exception {
		// WebElement errorElement = webDriver.waitForElement(
		// "//small[@class='error'][contains( text(),'" + messageText
		// + "')]", ByTypes.xpath, webDriver.getTimeout(), false,
		// "Error message is not displayed");

		messageText = messageText.replace("&nbsp;", " ");
		WebElement errorElement = webDriver.waitForElement(
				"//li[@class='siteLogin__messageText ng-binding'][contains(@ng-show,'"
						+ errorType + "')]", ByTypes.xpath,
				webDriver.getTimeout(), false);
		

		
			
		
		if (errorElement == null || errorElement.isDisplayed() == false) {
			testResultService.addFailTest("Error with text: " + messageText
					+ " was not found " + validatationStage);
		}
		else{
			testResultService.assertEquals(messageText, errorElement.getText(),
					"Text did not mached");
		}

	}

}
