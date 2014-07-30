package tests.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Objects.Course;
import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitonInteract1 extends EdusoftWebTest {

	private final String instructionText0 = "Click on the arrow next to the character you would like to practice.";
	private final String instructionText1 = "Click 'Start' to begin the conversation.";

	private final String instructionText3 = "Listen to the first speaker and prepare to speak.";
	private final String instructionText4 = "Prepare to speak.";
	private final String instructionText5 = "Speak now.";
	private final String instructionText6 = "Click 'Try again' to repeat your response.";

	@Before
	public void setup() throws Exception {
		super.setup();

	}

	// Test case 13309
	@Test
	public void testInteract1() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);
		String[] words = new String[] { "Hi", "I'm", "Tom", "Smith." };
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();

		startStep("Login to Edo");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 1");
		InteractSection interactSection = new InteractSection(webDriver);
	
		startStep("Select right speaker");
		
		interactSection.approveFlash();
		interactSection.checkInstructionText(instructionText0);

		sleep(1);
		interactSection.selectRightSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.checkInstructionText( instructionText1);
		sleep(1);
		
		
		startStep("Check of start button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkInstructionText( instructionText3);
	
		
		startStep("Wait for 3 seconds and start sending sound to the mic");
		sleep(3);
		
		startStep("Check the words level of the recording");
		String[]wordLevels=textService.splitStringToArray(interactSection.getDebugScore());
		interactSection.checkWordsLevels(words, wordLevels, textService);
		
		startStep("Click on restart and select left speaker");
		sleep(15);
		interactSection.clickTheRestartButton();
		interactSection.checkThatStartButtonIsDisabled();
		interactSection.selectLeftSpeaker();
		interactSection.checkThatSpeakerTextIsHighlighted(1);
		interactSection.clickTheStartButton();
		sleep(15);
		
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
