package tests.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Enums.InteractStatus;
import Objects.Course;
import Objects.Recording;
import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitonInteract1 extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();

	}

	// Test case 13309
	// First Discoveries,Unit 1 - About Me,I'm Tom Smith,6
	@Test
	public void testInteract1() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);

		String[] words;
		Recording recording = pageHelper.getRecordings().get(4);

		// List<String[]> recWordLevel = new ArrayList<String[]>();
		// List<Integer> sentenceLevels = new ArrayList<Integer>();

		startStep("Login to Edo");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 1");
		InteractSection interactSection = new InteractSection(webDriver,
				testResultService);

		startStep("Select right speaker");

		// interactSection.approveFlash();
		interactSection.checkInstructionText(interactSection.instructionText0);

		sleep(1);
		interactSection.selectRightSpeaker();
		interactSection.checkInstructionText(interactSection.instructionText8);
		interactSection.checkThatSpeakerTextIsHighlighted(2);
		interactSection.checkInstructionText(interactSection.instructionText1);
		sleep(1);

		startStep("Check of start button is enabled and click it");
		interactSection.clickTheStartButton();
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 2);
		interactSection.checkInstructionText(interactSection.instructionText3);

		startStep("Wait for 3 seconds and start sending sound to the mic");

		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 3);
		interactSection.checkInstructionText(interactSection.instructionText4);
		sleep(5);
		interactSection.allowMicFirefox();
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5);
		sleep(1);

		startStep("Send the 1st recording");
		words = interactSection.getCurrentSpeakerText(2, textService);
		audioService
				.sendSoundToVirtualMic(recording.getFiles().get(0), 8000.0F);
		// interactSection.waitUntilRecordingEnds(4, 2);

		interactSection
				.waitForInstructionToEnd(interactSection.instructionText5);

		// interactSection.checkStatus(InteractStatus.recorder, 2);

		startStep("Check the words level of the recording");
		String[] wordLevels = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(words);
		interactSection.checkInteract1WordsLevels(words, wordLevels,
				textService, 2);

		startStep("Wait for next recording");
		interactSection.checkInstructionText(interactSection.instructionText9);
		interactSection.checkIfInteract1StatusChanged(1,
				InteractStatus.speaker, 5);
		interactSection.checkInstructionText(interactSection.instructionText3);
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText4);
		sleep(3);
		interactSection.allowMicFirefox();
		interactSection.checkIfInteract1StatusChanged(2,
				InteractStatus.recorder, 20);
		interactSection.checkInstructionText(interactSection.instructionText5);
		audioService
				.sendSoundToVirtualMic(recording.getFiles().get(1), 0);
		sleep(1);
		interactSection
		.waitForInstructionToEnd(interactSection.instructionText5);
//		interactSection.waitUntilRecordingEnds(5, 2);
		interactSection.checkInstructionText(interactSection.instructionText10);
		startStep("Check the 2nd recording");
		words = interactSection.getCurrentSpeakerText(2, textService);
		wordLevels = textService.splitStringToArray(interactSection
				.getWordsScoring("debugScore"));
		System.out.println("Got words levels." + System.currentTimeMillis());
		textService.printStringArray(words);
		interactSection.checkInteract1WordsLevels(words, wordLevels,
				textService, 2);
		sleep(1);

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
