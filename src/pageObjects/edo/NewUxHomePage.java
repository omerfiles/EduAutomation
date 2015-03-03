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
		// TODO Auto-generated constructor stub
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
						"//div[@class='modal-header ng-scope']//h2[contains(@class,'title layout__text')]",
						ByTypes.xpath).getText();

		return text;
	}

	public void checkCustomAboutLink(String url, String label) throws Exception {
		String expectedText = webDriver.waitForElement(
				"//ul//li//a[@href='" + url + "']", ByTypes.xpath,
				webDriver.getTimeout(), false).getText();

		testResultService.assertEquals(expectedText, label);
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
		testResultService
				.assertEquals(
						whiteLabel,
						(webDriver
								.waitForElement(
										"/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/table/tbody/tr/td/span[2]",
										ByTypes.xpath).getText()));
		List<WebElement> WLList = webDriver
				.getElementsByXpath("//span[@class='disclamer" + whiteLabel
						+ "ng-binding']");
		for (int i = 0; i < WLList.size(); i++) {
			String text = WLList.get(i).getText();
			testResultService.assertEquals(whiteLabel, text);
		}

		webDriver.waitForElement("//div[2]/div[2]/div/div/a", ByTypes.xpath)
				.click();

		webDriver.sleep(3);

		this.clickOnLegalNotice();
		int startIndex = webDriver
				.waitForElement(
						"/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div",
						ByTypes.xpath).getText()
				.indexOf(whiteLabel + " Web Sites");
		testResultService
				.assertEquals(
						whiteLabel,
						(webDriver
								.waitForElement(
										"/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div",
										ByTypes.xpath).getText().substring(
								startIndex, startIndex + 3)));
		webDriver.waitForElement("//div[2]/div[2]/div/div/a", ByTypes.xpath)
				.click();

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
				"//div[@class='carousel-inner']", ByTypes.xpath);
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
}
