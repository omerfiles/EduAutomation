package pageObjects;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import services.AudioService;
import services.TextService;
import Enums.ByTypes;
import Enums.SRWordLevel;
import Objects.Recording;
import drivers.GenericWebDriver;

public class RecordPanel extends GenericPage {

	String rating1Text = "0%-16%";
	String rating2Text = "17%-33%";
	String rating3Text = "34%-50%";
	String rating4Text = "51%-66%";
	String rating5Text = "67%-83%";
	String rating6Text = "84%-100%";

	int numOfCurrentRecordings = 0;

	public RecordPanel(GenericWebDriver webDriver) {
		super(webDriver);
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

	public RecordPanel sendRecordingToMic(File file, AudioService audioService)
			throws Exception {
		audioService.sendSoundToVirtualMic(file);
		return this;

	}

	public void clickOnPlayButton() throws Exception {
		webDriver.waitForElement("btnHear", ByTypes.id).click();
	}

	public void selectRecording(String index) throws Exception {
		webDriver.waitForElement(
				"//ul[@id='ulURecords']//li[" + index + "]//a//input",
				ByTypes.xpath).click();
	}

	public void clickOnRecordButton() throws Exception {
		webDriver.waitForElement("btnRecord", ByTypes.id).click();
	}

	public void clickOnRecordAndStop(int seconds) throws Exception {
		webDriver.waitForElement("btnRecord", ByTypes.id).click();
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

	public void clickOnStopButton() throws Exception {
		webDriver.waitForElement("btnHear", ByTypes.id).click();
	}

	public void checkWordsLevel(String[] words, String[] wordsScores,
			TextService textService) throws Exception {
		for (int i = 0; i < words.length; i++) {
			CheckWordScore(words[i], Integer.valueOf(wordsScores[i]),
					textService);
		}
	}

	public void CheckWordScore(String word, int expectedWordLevel,
			TextService textService) throws Exception {
		boolean found = false;
		SRWordLevel wordLevel = null;
		if (expectedWordLevel <= 2) {
			wordLevel = SRWordLevel.failed;
		} else if (expectedWordLevel > 2 && expectedWordLevel <= 4) {
			wordLevel = SRWordLevel.pass;
		} else if (expectedWordLevel > 4 && expectedWordLevel <= 6) {
			wordLevel = SRWordLevel.success;
		}
		webDriver.waitForElement(
				"//div[@id='txtOriginal']//div//span[@class='"
						+ wordLevel.toString() + "'][contains(text(),"
						+ textService.resolveAprostophes(word) + ")]",
				ByTypes.xpath);

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

		webDriver.waitForElement(
				"//div[@class='srPanelScoreWrapper']//div[2]//span[@class='scoreSquares"
						+ ratingIndex + "']", ByTypes.xpath);
		String ratingText = null;
		String ratingClass = null;
		switch (ratingIndex) {
		case 1:
			ratingText = rating1Text;
			break;
		case 2:
			ratingText = rating2Text;
			break;
		case 3:
			ratingText = rating3Text;
			break;
		case 4:
			ratingText = rating4Text;
			break;
		case 5:
			ratingText = rating5Text;
			break;
		case 6:
			ratingText = rating6Text;
			break;
		}

		String actualSentenceScoreText = webDriver.waitForElement(
				"//div[@class='scoreRangeWrapper']", ByTypes.xpath).getText();
		Assert.assertEquals(
				"Sentence rating text does not matche or not found",
				ratingText, actualSentenceScoreText);
	}

	public void closeRecordPanel() throws Exception {
		webDriver.waitForElement("cboxClose", ByTypes.id).click();
	}

	public void clickOnSendToTeacher() throws Exception {
		webDriver.waitForElement("//div[@id='btnSendTT']//a", ByTypes.xpath)
				.click();
	}

	public String getWordsScoring() throws Exception {
		String str = webDriver.waitForElement("wl", ByTypes.id).getAttribute(
				"value");
		System.out.println("Random data is:" + str);
		str = str.replace(",", "");
		return str;

	}

	public int getSentenceLevel() throws Exception, Exception {
		int level = Integer.valueOf(webDriver.waitForElement("sl", ByTypes.id)
				.getAttribute("value"));
		return level;
	}

	public void checckSentenceLevelLightBulbs(int sentenceLevel)
			throws Exception {
		WebElement element = webDriver.waitForElement(
				"//div[@class='srPanelScoreWrapper']//div[2]//span[@class='scoreSquares"
						+ sentenceLevel + "']", ByTypes.xpath);
		System.out.println(getSLBackgroundImage(element));

		Assert.assertEquals(getExpectedGifBySL(sentenceLevel),
				getSLBackgroundImage(element));
	}

	public String getExpectedGifBySL(int sl) {
		String expectedGif = webDriver.getSutUrl()
				+ webDriver.getIntitutionName() + "/Runtime/Context/SRAPanel/";

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
		bgImg = bgImg.substring(4, bgImg.length() - 1);
		return bgImg;
	}

	public void checkAddedRecordingToList(int sentenceLevel, int index)
			throws Exception {
		index++;
		WebElement element = webDriver.waitForElement(
				"//ul[@id='ulURecords']//li[" + index + "]//a//span[3]",
				ByTypes.xpath);
		String bgImage = getSLBackgroundImage(element);
		Assert.assertEquals(getExpectedGifBySL(sentenceLevel), bgImage);
		System.out.println("checked that recording was added");

	}

	public void checkSendStutusMessage() throws Exception {
		String message = webDriver.waitForElement("divSendStatus", ByTypes.id)
				.getText();
		Assert.assertTrue(message
				.contains("Click Record to record this sentence again"));

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

}
