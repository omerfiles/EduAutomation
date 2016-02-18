package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class NewUxHomePage extends GenericPage {

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
						"//div[@class='ngsb-container']//h2[contains(@class,'title layout__text')]",
						ByTypes.xpath).getText();

		return text;
	}

	public void checkCustomAboutLink(String url, String label) throws Exception {
		testResultService.assertElementText(webDriver.waitForElement(
				"//a[@href='" + url + "']", ByTypes.xpath), label);
	}

	public void checkCustomContactUsLink(String mailto) throws Exception {
		webDriver.waitForElement("//a[contains(@href, 'mailto:" + mailto
				+ "')]", ByTypes.xpath);
	}

	public void checkCustomLogo(String logoLink, String imageFileName)
			throws Exception {
		webDriver
				.waitForElement("//a[@href='" + logoLink + "']", ByTypes.xpath);
		webDriver.waitForElement("//a/img[@src='Images/General/"
				+ imageFileName + "']", ByTypes.xpath);
	}

	public void checkCustomPrivacyLegal(String whiteLabel) throws Exception {
		this.clickOnPrivacyStatement();
		testResultService.assertEquals(whiteLabel,(webDriver.waitForElement("/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/table/tbody/tr/td/span[2]",
										ByTypes.xpath).getText()));
		List<WebElement> WLList = webDriver
				.getElementsByXpath("//span[@class='disclamer"+whiteLabel+"ng-binding']");
		for(int i=0;i<WLList.size();i++){
			String text=WLList.get(i).getText();
			testResultService.assertEquals(whiteLabel, text);
		}
				
		webDriver.waitForElement("//div[2]/div[2]/div/div/a", ByTypes.xpath).click();
		
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

}
