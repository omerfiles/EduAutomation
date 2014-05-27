package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import drivers.GenericWebDriver;

public class EdoLoginPage extends LoginPage {
	
	public EdoLoginPage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}
	protected WebElement usernameElement;
	protected WebElement passwordElement;
	protected WebElement loginButton;
	
	
	
	
	
//	By passwordElement=By.name("password");
//	By loginButton=By.linkText("LOGIN");
	
	public LoginPage typeUserNameAndPass(String userName,String password)throws Exception{
		webDriver.waitForElement("UserName", "id").sendKeys(userName);
		webDriver.waitForElement("Password", "name").sendKeys(password);
		return this;
	}
	
	public EdoHomePage submitLogin()throws Exception{
		webDriver.waitForElement("//div[@class='blueButton']", "xpath").click();
		return new EdoHomePage(webDriver);
	}
	public EdoLoginPage openEdoLoginPage()throws Exception{
		webDriver.openUrl("http://edonew.qa.com/5html.aspx");
		return this;
	}
	public EdoLoginPage openEdoLoginPage(String url)throws Exception{
		webDriver.openUrl(url);
		return this;
	}
	
	
	
	

}
