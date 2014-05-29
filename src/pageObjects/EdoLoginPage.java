package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Objects.Student;
import Objects.UserObject;
import drivers.GenericWebDriver;

public class EdoLoginPage extends LoginPage {

	public EdoLoginPage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	protected WebElement usernameElement;
	protected WebElement passwordElement;
	protected WebElement loginButton;

	// By passwordElement=By.name("password");
	// By loginButton=By.linkText("LOGIN");

//	public EdoHomePage login(UserObject userObject) throws Exception {
//
//		typeUserNameAndPass(userObject.getUserName(), userObject.getPassword());
//
//		return new EdoHomePage(webDriver);
//	}

	public LoginPage typeUserNameAndPass(String userName, String password)
			throws Exception {
		webDriver.waitForElement("UserName", "id").sendKeys(userName);
		webDriver.waitForElement("Password", "name").sendKeys(password);
		return this;
	}

	public EdoHomePage submitLogin() throws Exception {
		webDriver.waitForElement("//div[@class='blueButton']", "xpath").click();
		return new EdoHomePage(webDriver);
	}

	// public EdoLoginPage openEdoLoginPage(String url)throws Exception{
	// webDriver.openUrl(url);
	// return this;
	// }

	@Override
	public EdoLoginPage OpenPage(String url) throws Exception {
		webDriver.openUrl(url);
		return this;
	}

	public EdoHomePage login(UserObject user) throws Exception {
		typeUserNameAndPass(user.getUserName(), user.getPassword());
		webDriver.waitForElement("//div[@class='blueButton']", "xpath").click();
		return new EdoHomePage(webDriver);
	}

}
