package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxHomePage extends GenericPage {

	private static final String MENU_BTN = "sitemenu__openMenuBtn";
	private static final String LOGOUT_XPATH = "//button[@ng-click='logout()']";

	public NewUxHomePage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated cbbbbonstructor stub
	}

	public void clickOnAboutEdusoft() throws Exception {
		webDriver.waitForElement("About Edusoft", ByTypes.linkText).click();
	}

	public void clickOnLegalNotice() throws Exception {
		webDriver.waitForElement("Legal Notices", ByTypes.linkText).click();
	}

	public void clickOnPrivacyStatement() throws Exception {
		webDriver.waitForElement("Privacy Statement", ByTypes.linkText).click();
	}

	public String getOcModalTitleText() throws Exception {
		String text = webDriver
				.waitForElement(
						"//div[@class='modal-header ng-scope']//h2[(@class='modal-title ng-binding')]",
						
						ByTypes.xpath).getText();

		return text;
	}

	public void checkCustomAboutLink(String url, String label) throws Exception {

		String expectedText = webDriver.waitForElement(
				"//ul//li//a[@href='" + url + "']", ByTypes.xpath).getText();

		testResultService.assertEquals(label, expectedText);

	}

	public void checkCustomContactUsLink(String mailto) throws Exception {
		webDriver.waitForElement("//a[contains(@href, 'mailto:" + mailto
				+ "')]", ByTypes.xpath);
	}

	public void checkCustomLogo(String logoLink, String imageFileName)
			throws Exception {
		// webDriver
		// .waitForElement("//a[@href='" + logoLink + "']", ByTypes.xpath);
		webDriver.waitForElement("//a/img[@src='Images/General/"
				+ imageFileName + "']", ByTypes.xpath);
	}

	public void checkCustomPrivacyLegal(String whiteLabel) throws Exception {
		this.clickOnPrivacyStatement();
		
		testResultService.assertEquals(
				whiteLabel,
				(webDriver.waitForElement("//span[@class='disclamer"
						+ whiteLabel + " ng-binding']", ByTypes.xpath).getText()));
		
		List<WebElement> WLList = webDriver.getElementsByXpath("//span[@class='disclamer" + whiteLabel + " ng-binding']");

//		for (int i = 0; i < WLList.size(); i++)
		for (int i = 0; i < 10; i++)	
		
		{
				
			String text = WLList.get(i).getText();
			
			testResultService.assertEquals(whiteLabel, text);

		}

		webDriver.waitForElement("//a[@title='Close window']", ByTypes.xpath).click();

		webDriver.sleep(3);

		this.clickOnLegalNotice();
		int whiteLabelength = whiteLabel.length();
		int startIndex = webDriver
				.waitForElement("//td[@class='ng-binding']",
						ByTypes.xpath).getText()
				.indexOf(whiteLabel + " Web Sites");
		testResultService.assertEquals(
				whiteLabel,
				(webDriver.waitForElement("//td[@class='ng-binding']",
						ByTypes.xpath).getText().substring(startIndex,
						startIndex + whiteLabelength)));
		webDriver.waitForElement("//a[@title='Close window']", ByTypes.xpath).click();

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

	public void carouselNavigateNext() throws Exception {
		webDriver.waitForElement("//a[@ng-click='next()']", ByTypes.xpath)
				.click();

	}

	public void carouselNavigateBack() throws Exception {
		webDriver.waitForElement("//a[@ng-click='prev()']", ByTypes.xpath)
				.click();
	}

	public String getUnitName() throws Exception {
		WebElement webElement = webDriver.waitForElement(
				"//div[@class='carouselCaptions ng-scope']", ByTypes.xpath);
		System.out.println(webDriver.getElementHTML(webElement));
		return webElement.getText();

	}

	public void clickOnLogOut() throws Exception {
		// TODO Auto-generated method stub

		webDriver.waitForElement(LOGOUT_XPATH, ByTypes.xpath,
				webDriver.getTimeout(), false).click();

	}

	public void clickToOpenNavigationBar() throws Exception {
		webDriver.waitForElement(MENU_BTN, ByTypes.id, webDriver.getTimeout(),
				false).click();

	}

	public void getNavigationBarStatus() throws Exception {
		webDriver.waitForElement("//nav[contains(@class, 'layout__siteNav')]",
				ByTypes.xpath, webDriver.getTimeout(), false);

	}

	public String getNavBarItemsNotification(String id) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//ul[@class='sitemenu__menu']//li[" + id + "]", ByTypes.xpath,
				webDriver.getTimeout(), false);
		return element.getText();

	}

	public boolean isNavBarItemEnabled(String id) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//ul[@class='sitemenu__menu']//li[" + id
						+ "][@class='sitemenu__item disabled']", ByTypes.xpath,
				webDriver.getTimeout(), false);
		if (element != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNavBarItemAlerted(String id) throws Exception {
		WebElement element = webDriver.waitForElement(
				"//ul[@class='sitemenu__menu']//li[" + id
						+ "][@class='sitemenu__item alerted']", ByTypes.xpath,
				webDriver.getTimeout(), false);
		if (element != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNavBarOpen() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//ul[@class='sitemenu__menu']", ByTypes.xpath,
				webDriver.getTimeout(), false);
		if (element != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getUserDataText() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//span[@class='home__userName']", ByTypes.xpath,
				webDriver.getTimeout(), false);
		return element.getText();
	}
	
	
	
	public boolean isCourseCompletionWidgetExist() throws Exception {
	
		WebElement element = webDriver.waitForElement("//div[contains(@class,'home__studentWidgetCompletion')]", ByTypes.xpath);
		
	return element.isDisplayed();
	
	}
	
	public String getCourseCompletionWidgetUnitItem () throws Exception {
	
		WebElement element = webDriver.waitForElement("//div[@class = 'layout__pull itemUnit']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public String getCourseCompletionWidgetLabel (String CompletionLabel) throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[@title = '"+CompletionLabel+"']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public boolean isScoreWidgetExist() throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[contains(@class,'home__studentWidgetTestScores')]", ByTypes.xpath);
		
	return element.isDisplayed();
	
	}
	
	public String getScoreWidgetUnitItem () throws Exception {
	
		WebElement element = webDriver.waitForElement("//div[@class = 'layout__pull itemUnit']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public String getScoreWidgetLabel (String ScoreLabel) throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[@title = '"+ScoreLabel+"']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public boolean isTimeWidgetExist() throws Exception {
		
		WebElement element = webDriver.waitForElement("//div[contains(@class,'home__studentWidgetTimeOnTask')]", ByTypes.xpath);
		
	return element.isDisplayed();
	
	}
	
	public String getTimeWidgetUnitsDelimiter () throws Exception {
	
		WebElement element = webDriver.waitForElement("//span[@class = 'home__studentWidgetHrMinDots']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public String getTimeWidgetHoursLabel() throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[1][@class = 'home__studentWidgetHrMinHrTxt']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public String getTimeWidgetMinLabel() throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[3][@class = 'home__studentWidgetHrMinHrTxt']", ByTypes.xpath);
		
	return element.getText();
	
	}
	
	public String getTimeWidgetLabel (String TimeLabel) throws Exception {
		
		WebElement element = webDriver.waitForElement("//span[@title = '"+TimeLabel+"']", ByTypes.xpath);
		
	return element.getText();
	
	}
	

	
	
}
