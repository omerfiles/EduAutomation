package tests.edo.TableCompatability;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pageObjects.EdoHomePage;
import Enums.SubComponentName;
import Interfaces.TestCaseParams;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class DragAndDropTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "16581" })
	public void testDragAndDropClassification() throws Exception {
		// 6 Intermediate 2 Instructions 66 Listening 234 Forecast Practice
		// Classification 3 i2lrwep03 57117
		String[] words = new String[] { "warm", "clean", "heat", "cozy",
				"broken parts", "inspect", "water marks", "ceiling", "check",
				"leaks", "snow", "rain" };

		Course course = pageHelper.getCourses().get(18);

		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("2");
		sleep(3);
		report.startLevel("Start to drag and drop - fill all correct answers");
		edoHomePage.dragClassificationAnswerByTextFromBank("warm", 1, 1);
		edoHomePage.dragClassificationAnswerByTextFromBank("clean", 2, 1);
		edoHomePage.dragClassificationAnswerByTextFromBank("heat", 3, 1);
		edoHomePage.dragClassificationAnswerByTextFromBank("cozy", 4, 1);
		edoHomePage
				.dragClassificationAnswerByTextFromBank("broken parts", 5, 1);
		edoHomePage.dragClassificationAnswerByTextFromBank("inspect", 6, 1);

		edoHomePage.dragClassificationAnswerByTextFromBank("water marks", 1, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("ceiling", 2, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("check", 3, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("leaks", 4, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("snow", 5, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("rain", 6, 2);

		edoHomePage.clickOnCheckAnswers();

		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragAndDropCorrectAnswer(words[i]);
		}

		edoHomePage.clickOnClearAnswer();

		report.startLevel("Drop and replace from bank");
		edoHomePage.dragClassificationAnswerByTextFromBank("rain", 6, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank("leaks", 6, 2);
		edoHomePage.checkDragElementLocation("leaks", "34", 6, 2);
		edoHomePage.checkDragElementIsBackToBank("rain");

		report.startLevel("Drop and replace between used elements");
		edoHomePage.dragClassificationAnswerByTextFromBank("rain", 5, 2);

		edoHomePage.dragClassificationAnswerByTextFromExistingAnswer("rain", 6,
				2, 6, 2);
		edoHomePage.checkDragElementLocation("rain", "41", 6, 2);
		edoHomePage.checkDragElementIsBackToBank("leaks");
		report.startLevel("click on see answers");
		edoHomePage.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragElementIsPlaced(words[i]);
		}

		// TODO drag element to bank
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragClassificationAnswerByTextFromBank("check", 3, 2);
		edoHomePage.dragClassificationAnswerToBank("check");
		edoHomePage.checkDragElementIsBackToBank("check");

		report.startLevel("End of test");
	}

	@Test
	@TestCaseParams(testCaseID = { "16585" })
	public void testDragAndDropMatching() throws Exception {
		// Intermediate 2 Instructions 66 Reading 235 Flight Information
		// Practice Matching 2 i2rnfip002 24387
		Course course = pageHelper.getCourses().get(19);
		String[] words = new String[] { "claim", "inspection", "items",
				"complications", "card", };

		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("1");

		report.startLevel("Fill all answers");
		edoHomePage.dragClassificationAnswerByTextFromBank(words[0], 1, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[1], 2, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[2], 3, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[3], 4, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[4], 5, 2);

		report.startLevel("check answers");
		edoHomePage.clickOnCheckAnswers();
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragAndDropCorrectAnswer(words[i]);
		}

		report.startLevel("clear answers");
		edoHomePage.clickOnClearAnswer();

		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragElementIsBackToBank(words[i]);
		}

		report.startLevel("Drop and replace from bank");
		edoHomePage.dragClassificationAnswerByTextFromBank(words[0], 1, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[1], 1, 2);
		edoHomePage.checkDragElementIsBackToBank(words[0]);
		report.startLevel("Drop and replace between used elements");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragClassificationAnswerByTextFromBank(words[0], 1, 2);
		edoHomePage.dragClassificationAnswerByTextFromBank(words[1], 2, 2);

		edoHomePage.dragClassificationAnswerByTextFromExistingAnswer(words[0],
				6, 2, 2, 2);
		edoHomePage.checkDragElementIsBackToBank(words[1]);

		report.startLevel("click on see answers");
		edoHomePage.clickOnSeeAnswer();
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragElementIsPlaced(words[i]);
		}

	}

	@Test
	@TestCaseParams(testCaseID = { "16586" })
	public void testDragAndDropSequenceSentence() throws Exception {
		// Basic 2 Interesting People 46 Reading 132 Do It! Practice Sequence
		// Sentence 2 b2radip002 23043
		Course course = pageHelper.getCourses().get(20);
		String[] words = new String[] {
				"She hired some workers.",// 4
				"She decided that she wanted to tell others what to do.",// 2
				"She started the Clean-House Agency by putting an ad in a newspaper.",// 3
				"She taught the workers how to clean.",// 5
				"Her mother was always telling her what to do.",// 1
				"She decided to write cookbooks as a new business.",// 7
				"She started sending the workers out to homes and offices." };// 6
		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("1");
		sleep(5);
		webDriver.printScreen("Before dragging");
		report.startLevel("Arrange all answers");
		edoHomePage.dragSeqSentence(words[1], 2);
		edoHomePage.dragSeqSentence(words[0], 4);
		edoHomePage.dragSeqSentence(words[2], 3);
		edoHomePage.dragSeqSentence(words[3], 5);
		edoHomePage.dragSeqSentence(words[4], 1);
		edoHomePage.dragSeqSentence(words[5], 7);
		edoHomePage.dragSeqSentence(words[6], 6);

		report.startLevel("Check answer");
		edoHomePage.clickOnCheckAnswers();
		int index = 0;
		sleep(1);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[0], 4);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[1], 2);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[2], 3);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[3], 5);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[4], 1);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[5], 7);
		edoHomePage.checkSeqSentenceCorrectAnswer(words[6], 6);

		report.startLevel("Check wrong answer");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragSeqSentence(words[1], 4);
		edoHomePage.clickOnCheckAnswers();
		edoHomePage.checkSeqSentenceInCorrectAnswer(words[1], 4);

	}

	@Ignore
	@TestCaseParams(testCaseID = { "16587" })
	public void testDragAndDropSequenceimagetemplates() throws Exception {
		// 4 Basic 2 Buying And Selling 41 Reading 106 Wrong Color Practice
		// Sequence Image 4 b2rswcp004 22777
		String[] words = null;
		Course course = pageHelper.getCourses().get(21);

		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("2");

		report.startLevel("Check answer");
		edoHomePage.clickOnCheckAnswers();

		// for (int i = 0; i < words.length; i++) {
		// edoHomePage.checkDragAndDropCorrectAnswer(words[i]);
		// }
	}

	@Test
	@TestCaseParams(testCaseID = { "16589" })
	public void testDragAndDropCloze() throws Exception {
		// Advanced 1 Complaints 80 Reading 291 Complaint Practice Cloze 3
		// a1rlcop03 57206
		String[] words = new String[] { "Buys washing machine.",
				"Technician explains how machine works.",
				"Machine delivered and connected.",
				"Studies the instruction book thoroughly.",
				"Calls the department store.",
				"Technician comes to inspect machine.",
				"Threatens to take action with a lawyer." };
		Course course = pageHelper.getCourses().get(22);
		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		edoHomePage.ClickComponentStage("2");

		report.startLevel("Arrange answers in the correct order");
		String xpath = "//div[@class='TextDiv']//span[%s]";

		edoHomePage.dragAnserToElementByXpath(words[0], xpath, "1");
		edoHomePage.dragAnserToElementByXpath(words[2], xpath, "2");
		edoHomePage.dragAnserToElementByXpath(words[3], xpath, "3");
		edoHomePage.dragAnserToElementByXpath(words[1], xpath, "4");
		edoHomePage.dragAnserToElementByXpath(words[5], xpath, "5");
		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "6");
		edoHomePage.dragAnserToElementByXpath(words[6], xpath, "7");

		report.startLevel("Check answer");
		edoHomePage.clickOnCheckAnswers();
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragAndDropCorrectAnswerCloze(words[i]);
		}

		report.startLevel("Clear answers");
		edoHomePage.clickOnClearAnswer();

		report.startLevel("Check that all element are back in the bank");
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragElementIsBackToBankCloze(words[i]);
		}

		report.startLevel("Drop and replace from bank");

		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "6");
		edoHomePage.dragAnserToElementByXpath(words[6], xpath, "6");
		edoHomePage.checkDragElementLocationCloze("6", "70");

		report.startLevel("click on see answers and check that all correct answers are displayed");
		edoHomePage.clickOnSeeAnswer();
		edoHomePage.checkDragElementLocationCloze("1", "62");
		edoHomePage.checkDragElementLocationCloze("2", "89");
		edoHomePage.checkDragElementLocationCloze("3", "79");
		edoHomePage.checkDragElementLocationCloze("4", "66");
		edoHomePage.checkDragElementLocationCloze("5", "81");
		edoHomePage.checkDragElementLocationCloze("6", "54");
		edoHomePage.checkDragElementLocationCloze("7", "70");

		report.startLevel("Drag element to the bank");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "6");
		edoHomePage.dragClassificationAnswerToBankCloze(words[4]);
		edoHomePage.checkDragElementIsBackToBankCloze(words[4]);

	}

	@Test
	@TestCaseParams(testCaseID = { "16592" })
	public void testDragAndDropMatchTexttoPicture() throws Exception {
		// Basic 2 Healthy Eating 42 Listening 110 Food Practice Match Text To
		// Picture 1 b2lrfop01 57356
		Course course = pageHelper.getCourses().get(23);
		String[] words = new String[] { "a cucumber", "lettuce", "a plate",
				"meat", "dessert" };
		report.startLevel("Login and navigate to unit component");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(4);
		edoHomePage.navigateToCourseUnitComponent(course,
				SubComponentName.Practice);

		report.startLevel("Drag all ansers");
		String xpath = "//div[@id='ListPlaceHolder']//div[%s]//div";
		edoHomePage.dragAnserToElementByXpath(words[0], xpath, "2");
		edoHomePage.dragAnserToElementByXpath(words[1], xpath, "1");
		edoHomePage.dragAnserToElementByXpath(words[2], xpath, "3");
		edoHomePage.dragAnserToElementByXpath(words[3], xpath, "5");
		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "4");

		report.startLevel("Check answer");
		edoHomePage.clickOnCheckAnswers();

		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragAndDropCorrectAnswerCloze(words[i]);
		}

		report.startLevel("Clear answers");
		edoHomePage.clickOnClearAnswer();

		report.startLevel("Check that all element are back in the bank");
		for (int i = 0; i < words.length; i++) {
			edoHomePage.checkDragElementIsBackToBankCloze(words[i]);
		}

		report.startLevel("Drop and replace from bank");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "2");
		sleep(3);
		// edoHomePage.checkDragElementLocationPicture("2", words[4]);
		edoHomePage.dragAnserToElementByXpath(words[3], xpath, "2");
		edoHomePage.checkDragElementLocationPicture("2", "70");

		report.startLevel("click on see answers and check that all correct answers are displayed");
		edoHomePage.clickOnSeeAnswer();
		edoHomePage.checkDragElementLocationPicture("1", "91");
		edoHomePage.checkDragElementLocationPicture("2", "83");
		edoHomePage.checkDragElementLocationPicture("3", "68");
		edoHomePage.checkDragElementLocationPicture("4", "43");
		edoHomePage.checkDragElementLocationPicture("5", "70");

		report.startLevel("Drag element to the bank");
		edoHomePage.clickOnClearAnswer();
		edoHomePage.dragAnserToElementByXpath(words[4], xpath, "5");
		edoHomePage.dragClassificationAnswerToBankCloze(words[4]);
		edoHomePage.checkDragElementIsBackToBankCloze(words[4]);

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
