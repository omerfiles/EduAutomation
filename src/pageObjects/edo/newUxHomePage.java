package pageObjects.edo;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class newUxHomePage extends GenericPage {

	public newUxHomePage(GenericWebDriver webDriver,
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
