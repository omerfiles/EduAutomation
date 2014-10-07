package pageObjects.toeic;

import Enums.ByTypes;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.TestQuestionPage;
import services.TestResultService;

public class toeicQuestionPage extends TestQuestionPage {

	public toeicQuestionPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clickOnNextButton() throws Exception {
		webDriver.waitForElement("//div[@class='nextBT']//a", ByTypes.xpath,
				"Test question next button");

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

	@Override
	public void answerMultiCheckBoxQuestion(TestQuestion question) {
		// TODO Auto-generated method stub

	}

	public void answerCheckboxQuestion(String questionId, String answerId)
			throws Exception {
		webDriver.waitForElement(
				"//div[" + questionId + "]//div[" + questionId + answerId
						+ "]//div//div//div[2]",
				ByTypes.xpath,
				"answer checkbox for question " + questionId + " and answer: "
						+ answerId).click();
		;
	}

	@Override
	public void dragAnswer(TestQuestion question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playMedia() throws Exception {
		webDriver.waitForElement("div[@id='listenBtn']//a", ByTypes.xpath,
				"Play media button").click();

	}

	@Override
	public void answerCheckboxQuestion(TestQuestion question) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
