package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;

public class InteractSection extends GenericPage {

	public InteractSection(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	public void selectRightSpeaker() throws Exception {

		// WebElement element =
		// webDriver.waitForElement("bgImgContainerWrapper",
		// ByTypes.className);
		WebElement speakerElement = webDriver.waitForElement(
				"//div[@class='bgImgContainerWrapper']//div[3]//a", ByTypes.xpath);
		speakerElement.click();
		
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

	public void approveFlash() throws Exception {
		long flashTimeout = 2000;
		webDriver.waitForElement("//div[@id='srcont']//embed", ByTypes.xpath)
				.click();
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.TAB);
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.TAB);
		Thread.sleep(flashTimeout);
		webDriver.sendKey(Keys.ENTER);
		Thread.sleep(flashTimeout);
		// Click Tab 7 times
		int index = 7;
		for (int i = 0; i < index; i++) {
			webDriver.sendKey(Keys.TAB);
			Thread.sleep(flashTimeout);
		}

		webDriver.sendKey(Keys.ENTER);

	}

	public void clickTheStartButton() throws Exception {
		WebElement startButton = webDriver.waitForElement("Start",
				ByTypes.linkText);
		Assert.assertTrue(startButton.isEnabled());
		startButton.click();

	}

	public void clickTheRestartButton() throws Exception {
		webDriver.waitForElement("//a[@class='button startAgain']//span",
				ByTypes.xpath).click();
	}

	public void checkRecordIndicator() throws Exception {

	}

}
