package tests.speechRecognition;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import Enums.InteractStatus;
import Objects.Course;
import Objects.Recording;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionInteract2 extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
		setEnableLoggin(true);
		setLogFilter("Incomming message");
	}

	@Test
	public void testInteract2() throws Exception {
		startStep("Init test data");
		Recording recording = pageHelper.getRecordings().get(17);
		Course course = pageHelper.initCouse(10);
		List<String[]> recordedText = null;
		String[] wordLevels;
		startStep("Open course unit");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 2");
		InteractSection interactSection = new InteractSection(webDriver,
				testResultService);
		startStep("Click on Interact2 - start button");
		webDriver.printScreen("waitingForStartText");
		interactSection.checkInstructionText(interactSection.instructionText15);
		sleep(2);
		interactSection.clickInteract2StartButtn();
		interactSection.checkInstructionText(interactSection.instructionText20);
		startStep("Check that speaker text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
		// sleep(3);
		startStep("Check for 1st counter and recorder");
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter,
				10);
		interactSection.checkInstructionText(interactSection.instructionText4);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,
				5);
		interactSection.checkInstructionText(interactSection.instructionText5);

		startStep("Check that the correct text is highlighted and check score");

		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 0);
		interactSection
				.checkifInteract2StatusChanged(InteractStatus.counter, 5);
		interactSection.checkInstructionText(interactSection.instructionText9);
		recordedText.add(interactSection.getInteract2RecordedText(textService,
				2));
		// wordLevels=textService.splitStringToArray(interactSection.getWordsScoring("debugScore"));
		webDriver.printScreen("Checking word levels");
		// interactSection.checkInteract2recorderText(2,words,wordLevels,textService);

		startStep("Check for 2nd counter and recorder");
		interactSection.checkThatSpeakerTextIsHighlighted();

		// interactSection.checkInstructionText(interactSection.instructionText4);
		sleep(3);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,
				5);
		audioService.sendSoundToVirtualMic(recording.getFiles().get(1), 0);
		interactSection.checkInstructionText(interactSection.instructionText5);
		recordedText.add(interactSection.getInteract2RecordedText(textService,
				1));
		// wordLevels=textService.splitStringToArray(interactSection.getWordsScoring("debugScore"));
		webDriver.printScreen("Checking word levels");
		// interactSection.checkInteract2recorderText(2,words,wordLevels,textService);

		startStep("wait untile playback ends");

		sleep(2);
		interactSection.checkInstructionText(interactSection.instructionText10);
		interactSection.clickOnSeeFeedback();
		
		startStep("Check interact final view");
		interactSection.clickOnListenToAllButton();

		startStep("Check recoreded text's word levels");
//		interactSection.checkFinalViewWordLevels(speakerText.get(0),wordLevels.get(0),textService,2);
//		interactSection.checkFinalViewWordLevels(speakerText.get(1),wordLevels.get(1),textService,4);
		
		

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
