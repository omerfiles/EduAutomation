package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import services.DbService;
import Enums.ByTypes;
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
		dbService.setUserLoginToNull(user.getId());
		typeUserNameAndPass(user.getUserName(), user.getPassword());
		webDriver.waitForElement("//div[@class='blueButton']", "xpath").click();
		EdoHomePage edoHomePage=new EdoHomePage(webDriver);
		edoHomePage.waitForPageToLoad();
		return edoHomePage;
		
	}

	public EdoLoginPage clickOnSelfRegistraton()throws Exception {
		webDriver.waitForElement("Self Registration", ByTypes.linkText.toString()).click();
		return this;
		
	}

	public EdoLoginPage enterStudentRegUserName(String studentName)throws Exception {
		report.report("User name is: "+studentName);
		webDriver.waitForElement("UserName", "id").sendKeys(studentName);
		return this;
		
	}
	public EdoLoginPage enterStudentRegFirstName(String studentName)throws Exception {
		webDriver.waitForElement("FirstName", "id").sendKeys(studentName);
		return this;
		
	}
	public EdoLoginPage enterStudentRegLastName(String studentName)throws Exception {
		webDriver.waitForElement("LastName", "id").sendKeys(studentName);
		return this;	
	}

	public EdoLoginPage enterStudentRegPassword(String password)throws Exception {
		webDriver.waitForElement("Password", "id").sendKeys(password);
		webDriver.waitForElement("ConfirmPassword", "id").sendKeys(password);
		return this;	
	}

	public EdoLoginPage enterStudentEmail(String email)throws Exception {
		webDriver.waitForElement("Email", "id").sendKeys(email);
		return this;
	}

	public EdoLoginPage clickOnRegister()throws Exception {
		webDriver.waitForElement("Submit", "id").click();
		return this;
	}

}
