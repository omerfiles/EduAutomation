package pageObjects.edo;

import java.util.List;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.PLTStartLevel;
import Enums.PLTStartLevel;
import Enums.TestQuestionType;
import Objects.PLTCycle;
import Objects.TestQuestion;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import pageObjects.TestQuestionPage;
import services.TestResultService;

public class edoPlacementTestPage extends TestQuestionPage {

	public edoPlacementTestPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	public void answerMultiCheckBoxQuestion(TestQuestion question) throws Exception {
		for(int i=0;i<question.getCorrectAnswers().length;i++){
			webDriver.waitForElement(
					"//span[@class='multiTextInline'][contains(text(),'"
							+ question.getCorrectAnswers()[i] + "')]//",
					ByTypes.xpath).click();
		}

	}

	@Override
	public void clickOnNextButton() throws Exception {
		webDriver.waitForElement("nextQuest", ByTypes.id, "Next button")
				.click();

	}

	public void dragAnswer(String answerId, String questionLocationId)
			throws Exception {
		WebElement from = webDriver.waitForElement(
				"//div[@data-id='" + answerId + "']",
				ByTypes.xpath,
				"element of answer number text "
						+ questionLocationId.toString());
		WebElement to = webDriver.waitForElement("//span[@data-id='"
				+ questionLocationId + "']", ByTypes.xpath);
		webDriver.dragAndDropElement(from, to);

	}

	public void answerDragQuestionSingle(TestQuestion question)
			throws Exception {
		String[] answerSourceLocations = question.getCorrectAnswers();
		String[] answerDestinationLocations = question.getAnswersDestinations();
		for (int i = 0; i < answerSourceLocations.length; i++) {
			WebElement from = webDriver.waitForElement(
					"//div[@data-id='" + answerSourceLocations[i] + "']",
					ByTypes.xpath,
					"element of answer number text "
							+ answerSourceLocations[i].toString());
			WebElement to = webDriver.waitForElement("//span[@data-id='"
					+ answerDestinationLocations[i] + "']", ByTypes.xpath);
			webDriver.dragAndDropElement(from, to);
		}
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
	public void playMedia() throws Exception {
		webDriver.waitForElement("CTrackerPlayBtn", ByTypes.id,
				"Play media button").click();

	}

	@Override
	public void answerCheckboxQuestion(TestQuestion question) throws Exception {

		webDriver.waitForElement(
				"//span[@class='multiTextInline'][contains(text(),'"
						+ question.getCorrectAnswers()[0] + "')]//",
				ByTypes.xpath).click();

	}

	public void selectAnswersFromComboxBox(TestQuestion question) throws Exception {

	}

//	public void answerQuestion(TestQuestion question) {
//		// Check question type
//		switch (question.getQuestionType()) {
//		case Basic:
//		case Advance:
//		case Intermediate:
//		case IamNotSure:
//
//		}
//		;
//	}

	public void selectStartLevel(PLTStartLevel level) throws Exception {
		switch (level) {
		case Basic:
			webDriver.waitForElement("rbBasic", ByTypes.id,
					"Basic level button").click();
		case Advance:
			webDriver.waitForElement("rbIntermediate", ByTypes.id,
					"Intermediate level button").click();
		case Intermediate:
			webDriver.waitForElement("rbAdvanced", ByTypes.id,
					"Advance level button").click();
		case IamNotSure:
			webDriver.waitForElement("rbImNotSure", ByTypes.id,
					"Im not sure button").click();

		}
		;
		webDriver.waitForElement("Submit", ByTypes.name).click();

	}

	public void performTest(PLTCycle firstCycle, PLTCycle secondsCycle)
			throws Exception {
		// Fill the test questions according to the questions arrayList filled
		// from CSV file

		// Go over all 1st cycle questions and answer them according to the
		// questions object in the 1st PLTCyccle object

		// Cycle 1 has 17-20 questions (17 for basic/inter./advances and 20 for
		// "Im not sure"

		try {
			for (int i = 0; i < firstCycle.getNumberOfQuestions(); i++) {
				TestQuestion question = firstCycle.getCycleQuestions().get(i);
				TestQuestionType questionType = question.getQuestionType();
				switch (questionType) {
				case DragAndDropMultiple:
					
				case RadioMultiple:
					answerMultiCheckBoxQuestion(question);
					;
				case RadioSingle:
					;
				case DragAndDropSingle:
					answerDragQuestionSingle(firstCycle.getCycleQuestions()
							.get(i));
					;
				case TrueFalse:
					;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void dragAnswer(TestQuestion question) throws Exception {
		// TODO Auto-generated method stub
		
	}

}