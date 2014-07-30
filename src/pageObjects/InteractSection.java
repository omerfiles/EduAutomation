package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;

public class InteractSection extends SRpage {

	public InteractSection(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	public void selectRightSpeaker() throws Exception {
		WebElement speakerElement = webDriver.waitForElement(
				"//div[@class='bgImgContainerWrapper']//div[3]//a",
				ByTypes.xpath);
		speakerElement.click();
	}

	public void selectLeftSpeaker() throws Exception {
		WebElement speakerElement = webDriver.waitForElement(
				"//div[@class='bgImgContainerWrapper']//div[1]//a",
				ByTypes.xpath);
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
		webDriver.waitForElement("//a[@class='button startAgain']",
				ByTypes.xpath).click();
	}

	public void checkRecordIndicator() throws Exception {

	}

	public void checkInstructionText(String text) throws Exception {
		String actualText = webDriver.waitForElement(
				"//div[@class='questionInstructions']", ByTypes.xpath,
				"instruction text not found").getText();
		Assert.assertEquals("Text not found or do not match", text, actualText);
	}

	public void checkThatSpeakerTextIsHighlighted(int speaker) throws Exception {
		// right speaker{
		if (speaker == 1) {
			webDriver
					.waitForElement(
							"//div[@class='recordingPanelWrapper']//div[1][contains(@class,'selected')]",
							ByTypes.xpath);
		}
		// left speaker
		if (speaker == 2) {
			webDriver
					.waitForElement(
							"//div[@class='recordingPanelWrapper']//div[2][contains(@class,'selected')]",
							ByTypes.xpath);

		}

	}

	public void checkThatStartButtonIsDisabled() throws Exception {
		WebElement startButton = webDriver.waitForElement("Start",
				ByTypes.linkText);
		Assert.assertTrue(startButton.getAttribute("class").contains("disable") == false);
	}

	public String getDebugScore() throws Exception {
		String score = webDriver.waitForElement("debugScore", ByTypes.id)
				.getText();
		return score;

	}

}
