package tests.toeic;

import org.openqa.selenium.TimeoutException;

import Enums.ByTypes;
import pageObjects.toeic.toeicQuestionPage;
import pageObjects.toeic.toeicStartPage;
import tests.misc.EdusoftWebTest;

public class toeicBaseTest extends EdusoftWebTest {

	protected toeicStartPage startPage;
//	protected toeicQuestionPage questionPage;

	private final String startPageUrl = "http://toeic.prod.com//Intro.aspx?uId=000000000010";

	//Test inputs - answers.csv file containing question number, correct answer, incorrect answers
	//Expected score according to DB
	//Each test will have the params - number of correct and incorrect answers
	public void openToeicTest() throws TimeoutException, Exception {

		toeicStartPage toeicStartPage = new toeicStartPage(webDriver,
				testResultService);
		startPage = toeicStartPage.OpenPage(startPageUrl);
		// webDriver.openUrl("http://toeic.prod.com//Intro.aspx?uId=000000000010");
	}

	public toeicQuestionPage ClickOnStart() throws Exception {
		webDriver.waitForElement("//div[@class='StartBT']//a", ByTypes.xpath,
				"Teoic Start button").click();;
		return new toeicQuestionPage(webDriver, testResultService);
	}
	
	

}
