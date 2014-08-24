package pageObjects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import services.AudioService;
import services.TestResultService;
import services.TextService;
import Enums.ByTypes;
import Enums.SRWordLevel;
import Objects.Recording;
import drivers.ChromeWebDriver;
import drivers.FirefoxWebDriver;
import drivers.GenericWebDriver;

public class RecordPanel extends SRpage {

	String rating1Text = "0%-16%";
	String rating2Text = "17%-33%";
	String rating3Text = "34%-50%";
	String rating4Text = "51%-66%";
	String rating5Text = "67%-83%";
	String rating6Text = "84%-100%";

	String rating1FeedbackText = "Try Again";
	String rating2FeedbackText = "Try Again";
	String rating3FeedbackText = "Keep Working!";
	String rating4FeedbackText = "Good!";
	String rating5FeedbackText = "Very Good!";
	String rating6FeedbackText = "Excellent!";

	int numOfCurrentRecordings = 0;

	public RecordPanel(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
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

	// public RecordPanel sendRecordingToMic(File file, AudioService
	// audioService)
	// throws Exception {
	// audioService.sendSoundToVirtualMic(file);
	// return this;
	//
	// }

	public void clickOnPlayButton() throws Exception {
		webDriver.waitForElement("btnHear", ByTypes.id).click();
	}

	public void selectRecording(String index) throws Exception {
		WebElement radioBtn = webDriver.waitForElement(
				"//ul[@id='ulURecords']//li[" + index + "]//a//input",
				ByTypes.xpath);

		radioBtn.click();

		System.out.println("Checking the record was actually selected");
		boolean selected = webDriver.waitForElement(
				"//ul[@id='ulURecords']//li[" + index + "]//a//input",
				ByTypes.xpath).isSelected();
		if(selected==false){
			System.out.println("record was not selected. try again");
			radioBtn.click();
		}
	}

	public void clickOnRecordButton() throws Exception {
		webDriver.waitForElement("btnRecord", ByTypes.id).click();

		// approve popup using Java robot

		webDriver.sleep(1000);
		if (webDriver.getSutUrl().contains("develop")) {
			allowMicFirefox();
		}
		// **************************

		// webDriver.closeAlertByAccept();

		// checkThatHearButtonIsDisabled();
	}

	public void clickOnRecordAndStop(int seconds) throws Exception {
		webDriver.waitForElement("btnRecord", ByTypes.id).click();

		// webDriver.checkThatElementBecameDisabled(webDriver.waitForElement(
		// "btnHear", ByTypes.id));
		numOfCurrentRecordings++;

		int elapsedTime = 0;
		while (elapsedTime < seconds) {
			Thread.sleep(1000);
			elapsedTime++;
			report.report("Waiting for recording time to end");
		}
		report.report("clicking the stop button");
		// clickOnStopButton();
		checkNumberOfRecordings();

	}

	private void checkThatHearButtonIsDisabled() throws Exception {

		Assert.assertFalse("Hear button is not disabled", webDriver
				.waitForElement("btnHear", ByTypes.id).isEnabled());

	}

	public void clickOnStopButton() throws Exception {
		webDriver.waitForElement("btnHear", ByTypes.id).click();
	}

	public void checkSentenceScoreText(String expectedScore) throws Exception {
		String actualSentenceScore = webDriver.waitForElement(
				"//div[@class='srPanelScoreWrapper']//div[1]//div[1]",
				ByTypes.xpath).getText();
		System.out.println(actualSentenceScore);
		webDriver.printScreen("panel sentence score");
	}

	public void checkSentenceScoreRatingText(int ratingIndex) throws Exception {
		report.report("Checking sentence score rating. Expected rating: "
				+ ratingIndex);
		webDriver.printScreen("checkSentenceScoreRatingText");
		webDriver.waitForElement(
				"//div[@class='srPanelScoreWrapper']//div[2]//span[@class='scoreSquares"
						+ ratingIndex + "']", ByTypes.xpath);
		String ratingText = null;
		String ratingFeedbackText = null;
		switch (ratingIndex) {
		case 1:
			ratingText = rating1Text;
			ratingFeedbackText = rating1FeedbackText;
			break;
		case 2:
			ratingText = rating2Text;
			ratingFeedbackText = rating2FeedbackText;
			break;
		case 3:
			ratingText = rating3Text;
			ratingFeedbackText = rating3FeedbackText;
			break;
		case 4:
			ratingText = rating4Text;
			ratingFeedbackText = rating4FeedbackText;
			break;
		case 5:
			ratingText = rating5Text;
			ratingFeedbackText = rating5FeedbackText;
			break;
		case 6:
			ratingText = rating6Text;
			ratingFeedbackText = rating6FeedbackText;
			break;
		}

		String actualSentenceScoreText = webDriver.waitForElement(
				"//div[@class='scoreRangeWrapper']", ByTypes.xpath).getText();

		WebElement actualSentenceScore = webDriver.waitForElement(
				"//div[@class='scoreRangeWrapper']", ByTypes.xpath);

		// Assert.assertEquals(
		// "Sentence rating text does not matche or not found",
		// ratingText, actualSentenceScoreText);
		testResultService.assertEquals(ratingText, actualSentenceScoreText);

		String actualSentenceFeedbackText = webDriver.waitForElement(
				"//div[@class='scoreExpWrapper']", ByTypes.xpath).getText();

		// Assert.assertEquals(
		// "Sentence rating feedback text does not matche or not found",
		// ratingFeedbackText, actualSentenceFeedbackText);
		testResultService.assertEquals(ratingFeedbackText,
				actualSentenceFeedbackText);

	}

	public void closeRecordPanel() throws Exception {
		webDriver.waitForElement("cboxClose", ByTypes.id).click();
	}

	public void clickOnSendToTeacher() throws Exception {
		webDriver.waitForElement("//div[@id='btnSendTT']//a", ByTypes.xpath)
				.click();
	}

	public String[] getSentenceText(TextService textService) throws Exception {
		String text = webDriver.waitForElement("txtOriginal", ByTypes.id)
				.getText();
		System.out.println("words text is: " + text);
		text = text.replaceAll("[-.!]", "");
		text = text.trim();
		String[] words = textService.splitStringToArray(text, "\\s+");

		return words;
	}

	public int getSentenceLevel() throws Exception {
		int level = 0;
		try {
			level = Integer.valueOf(webDriver.waitForElement("sl", ByTypes.id)
					.getAttribute("value"));

		} catch (Exception e) {
			System.out.println("Problem getting sentence level");
			webDriver.printScreen();
		}

		return level;
	}

	public void checckSentenceLevelLightBulbs(int sentenceLevel)
			throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='srPanelScoreWrapper']//div[2]//span[@class='scoreSquares"
						+ sentenceLevel + "']", ByTypes.xpath);
		System.out.println("SL background image is (SL is " + sentenceLevel
				+ ") " + getSLBackgroundImage(element));

		// Assert.assertEquals(getExpectedGifBySL(sentenceLevel),getSLBackgroundImage(element));
		testResultService.assertEquals(getExpectedGifBySL(sentenceLevel),
				getSLBackgroundImage(element));
	}

	public String getExpectedGifBySL(int sl) {
		String expectedGif = null;

		if (webDriver.getSutUrl().contains("develop")) {
			expectedGif = webDriver.getSutUrl() + "Runtime/Context/SRAPanel/";
		} else {
			expectedGif = webDriver.getSutUrl() + webDriver.getIntitutionName()
					+ "/Runtime/Context/SRAPanel/";
		}

		switch (sl) {
		case 1:
			expectedGif = expectedGif + "srphistfb0icn.gif";
			break;
		case 2:
			expectedGif = expectedGif + "srphistfb1icn.gif";
			break;
		case 3:
			expectedGif = expectedGif + "srphistfb2icn.gif";
			break;
		case 4:
			expectedGif = expectedGif + "srphistfb3icn.gif";
			break;
		case 5:
			expectedGif = expectedGif + "srphistfb4icn.gif";
			break;
		case 6:
			expectedGif = expectedGif + "srphistfb5icn.gif";
			break;

		default:
			break;
		}
		return expectedGif;
	}

	public void checkNumberOfRecordings() throws Exception {
		WebElement recoredingList = webDriver.waitForElement("ulURecords",
				ByTypes.id);
		List<WebElement> recordingElements = recoredingList.findElements(By
				.xpath("//li"));
		System.out
				.println("number of recordings:  " + recordingElements.size());

	}

	public String getSLBackgroundImage(WebElement element) throws Exception {
		String bgImg = webDriver.getCssValue(element, "background-image");
		System.out.println("bgImage is: " + bgImg);
		try {
			if (webDriver instanceof FirefoxWebDriver) {
				bgImg = bgImg.substring(5, bgImg.length() - 2);
			} else if (webDriver instanceof ChromeWebDriver) {
				bgImg = bgImg.substring(4, bgImg.length() - 1);
			}
		} catch (Exception e) {
			testResultService.addFailTest("Getting SL image failed");
		}

		// bgImg=bgImg.replace("''", "");
		return bgImg;
	}

	public void checkAddedRecordingToList(int sentenceLevel, int index)
			throws Exception {
		index++;
		WebElement element = webDriver.waitForElement(
				"//ul[@id='ulURecords']//li[" + index + "]//a//span[3]",
				ByTypes.xpath);
		String bgImage = getSLBackgroundImage(element);
		Assert.assertArrayEquals("Image not found",
				new String[] { getExpectedGifBySL(sentenceLevel) },
				new String[] { bgImage });
		System.out.println("checked that recording was added");

	}

	public void checkSendStatusMessage() throws Exception {
		String message = webDriver.waitForElement("divSendStatus", ByTypes.id)
				.getText();
		Assert.assertTrue(message
				.contains("Click 'Record' to record this sentence again"));

	}

	public void clickOnSendStatusCancelButton() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@id='btnCancel4Send']//a", ByTypes.xpath);
		String elementText = element.getText();
		Assert.assertEquals("Cancel", elementText);
		element.click();

	}

	public void clickOnSendStatusRecordButton() throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@id='btnOK4Send']//a", ByTypes.xpath);
		Assert.assertEquals("Record", element.getText());
		element.click();

	}

	public void checkSendToTeacherText() throws Exception {
		String text = webDriver.waitForElement("divSendStatus", ByTypes.id)
				.getText();
		Assert.assertArrayEquals(
				"Text does not match",
				new String[] { "You have sent the maximum (2) number of recordings for this lesson. If you send this recording, your first sent recording will be deleted." },
				new String[] { text });

	}

	public void clickOnRecordAndSendRecording(File file) {

	}

	public String getRecordPanelStatus() throws Exception {
		String text = null;
		WebElement element = webDriver.waitForElement(
				"//div[@id='divRStatus']", ByTypes.xpath, true, 30);
		text = element.getText();
		System.out.println("SPEAK status found." + System.currentTimeMillis());
		return text;

	}

	public void waitForRecordingToEnd(int indexOfRecording) throws Exception {
		webDriver.waitForElement("//ul[@id='ulURecords']//li["
				+ indexOfRecording + "]//a//span[3]", ByTypes.xpath, true, 20);

	}

	public void checkThatWlIsCloseToExpectedWL(String[] expectedWL,
			String[] actualWL) {
		for (int i = 0; i < expectedWL.length; i++) {
			int expWL = Integer.valueOf(expectedWL[i]);
			int actWL = Integer.valueOf(actualWL[i]);
			System.out.println("Diffrence is: " + Math.abs(expWL - actWL));
			if (Math.abs(expWL - actWL) > 1) {
				System.out
						.println("Diffrence between expected WL and actual WL was bigger then 1");

			}
		}

	}
	public void clickOnRecordButtonAndSendRecording(Recording recording,float sampleRate,AudioService audioService) throws Exception{
		clickOnRecordButton();
		String status = getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		// recordPanel.waitForSpeakStatus();
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				sampleRate);

		
		Thread.sleep(2000);
		waitForRecordingToEnd(1);
	}

}
