package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import services.TextService;
import Enums.ByTypes;
import Enums.InteractStatus;
import Enums.SRWordLevel;
import drivers.GenericWebDriver;

public class InteractSection extends SRpage {

	public final String instructionText0 = "Click on the arrow next to the character you would like to practice.";
	public final String instructionText1 = "Click 'Start' to begin the conversation.";
	public final String instructionText2 = "Click 'Hear all' to hear the whole conversation. ";
	public final String instructionText3 = "Listen to the first speaker and prepare to speak.";
	public final String instructionText4 = "Prepare to speak.";
	public final String instructionText5 = "Speak now.";
	public final String instructionText6 = "Click 'Try again' to repeat your response.";
	public final String instructionText7 = "Your answer is unclear. Let's move on… ";
	public final String instructionText8 = "Click 'Start' to begin the conversation.";
	public final String instructionText9 = "Good! Let's move on...";
	public final String instructionText10 = "You have completed the conversation. Click ‘See feedback’ below to view detailed feedback on your responses.";
	public final String instructionText11 = "Listen to the response.";
	public final String instructionText12 = "Start";
	public final String instructionText13 = "Try again";
	public final String instructionText14 = "See feedback";
	public final String instructionText20 = "Listen to the first speaker and choose a response.";

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

	public void checkStatus(InteractStatus status, int speaker)
			throws Exception {
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "][contains(@class,'" + status.toString() + "')]",
				ByTypes.xpath);
	}

	public String[] getCurrentSpeakerText(int speaker, TextService textService)
			throws Exception {
		String text = null;
		text = webDriver.waitForElement(
				"//div[@class='recordingPanelWrapper']//div[" + speaker
						+ "]//div", ByTypes.xpath).getText();
		System.out.println("words text is: " + text);
		text = text.substring(1, text.length());
		String[] words = textService.splitStringToArray(text, "\\s+");
		return words;
	}

	public void waitUntilStatusChanges(int speaker, InteractStatus after,
			int timeOut) throws Exception {
		long timeBefore = System.currentTimeMillis();
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "][contains(@class,'" + after.toString() + "')]",
				ByTypes.xpath);
		WebElement afterElement = webDriver.waitForElement(
				"//div[@class='recordingPanelWrapper']//div[" + speaker
						+ "][contains(@class,'" + after.toString() + "')]",
				ByTypes.xpath, timeOut, true, null, 250);
		webDriver.printScreen("StatusChangedTo" + after.toString());
		Assert.assertNotNull("Status did not changed", afterElement);
		long timeAfter = System.currentTimeMillis();
		long time = timeAfter - timeBefore;
		System.out.println("Changed after :" + time + " ms");
		Assert.assertTrue("Status did not changed in time",
				time < timeOut * 1100);

	}

	public void waitUntilRecordingEnds(int timeOut, int speaker)
			throws Exception {
		webDriver.printScreen("Checking of recording ended");
		long timeBefore = System.currentTimeMillis();
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "]//div//div//div//span", ByTypes.xpath);
		webDriver.printScreen("recording ended");
		long timeAfter = System.currentTimeMillis();
		long time = timeAfter - timeBefore;
		System.out.println("Changed after :" + time + " ms");
		Assert.assertTrue("Status did not changed in time",
				time < timeOut * 1100);

	}

	public void checkInteractWordsLevels(String[] words, String[] wordLevels,
			TextService textService, int speaker) throws NumberFormatException,
			Exception {
		for (int i = 0; i < words.length; i++) {
			CheckInteractWordScore(words[i], Integer.valueOf(wordLevels[i]),
					textService, speaker);
		}

	}

	private void CheckInteractWordScore(String word, int expectedWordLevel,
			TextService textService, int speaker) throws Exception {
		boolean found = false;
		SRWordLevel wordLevel = null;
		if (expectedWordLevel <= 2) {
			wordLevel = SRWordLevel.failed;
		} else if (expectedWordLevel > 2 && expectedWordLevel <= 4) {
			wordLevel = SRWordLevel.pass;
		} else if (expectedWordLevel > 4 && expectedWordLevel <= 6) {
			wordLevel = SRWordLevel.success;
		}
		webDriver.waitForElement("//div[@class='recordingPanelWrapper']//div["
				+ speaker + "]//div//div//div//span[contains(text(),"
				+ textService.resolveAprostophes(word) + ")]", ByTypes.xpath);

	}

	public void clickInteract2StartButtn() throws Exception {
		webDriver.waitForElement("Start", ByTypes.linkText).click();

	}

	public void checkThatSpeakerTextIsHighlighted() throws Exception{
		webDriver.waitForElement("//div[@class='mediaContainer']//div[1][contains(@class,'speaker')]", ByTypes.xpath);
		
	}
	public void checkInteract2Status(InteractStatus status)throws Exception{
//		webDriver.waitForElement("//div[@class='speakingInteract']//div[2], byType)
	}

}
