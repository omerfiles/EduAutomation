package tests.speechRecognition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import Enums.InteractStatus;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionInteract2 extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testInteract2()throws Exception{
		startStep("Init test data");
	
		Course course = pageHelper.initCouse(10);
		String[] words;
		String[]wordLevels;
		startStep("Open course unit");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(),
				"Interact 2");
		InteractSection interactSection = new InteractSection(webDriver);
		startStep("Click on Interact2 - start button");
		interactSection.checkInstructionText(interactSection.instructionText1);
		sleep(2);
		interactSection.clickInteract2StartButtn();
		interactSection.checkInstructionText(interactSection.instructionText20);
		startStep("Check that speaker text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
//		sleep(3);
		startStep("Check for 1st counter and recorder");
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter,10);
		interactSection.checkInstructionText(interactSection.instructionText4);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,5);
		interactSection.checkInstructionText(interactSection.instructionText5);
		
		startStep("Check that the correct text is highlighted and check score");
		
		
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter,5);
		
		
		interactSection.checkInstructionText(interactSection.instructionText9);
		words=interactSection.getInteract2RecordedText(textService,2);
		wordLevels=textService.splitStringToArray(interactSection.getWordsScoring("debugScore"));
		webDriver.printScreen("Checking word levels");
		interactSection.checkInteract2recorderText(2,words,wordLevels,textService);
		
		startStep("Check for 2nd counter and recorder");
		interactSection.checkThatSpeakerTextIsHighlighted();
		
		interactSection.checkifInteract2StatusChanged(InteractStatus.counter,5);
//		interactSection.checkInstructionText(interactSection.instructionText4);
		sleep(3);
		interactSection.checkifInteract2StatusChanged(InteractStatus.recorder,5);
		interactSection.checkInstructionText(interactSection.instructionText5);
		words=interactSection.getInteract2RecordedText(textService,1);
		wordLevels=textService.splitStringToArray(interactSection.getWordsScoring("debugScore"));
		webDriver.printScreen("Checking word levels");
		interactSection.checkInteract2recorderText(2,words,wordLevels,textService);
		
		startStep("wait untile playback ends");
		
		
		interactSection.checkInstructionText(interactSection.instructionText10);
		interactSection.clickOnSeeFeedback();
		
		
		
		
		
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
