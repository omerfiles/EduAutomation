package tests.speechRecognition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.InteractSection;
import Objects.Course;
import tests.misc.EdusoftWebTest;

public class SpeechRecognitionInteract2 extends EdusoftWebTest {
	
	@Before
	public void setup()throws Exception{
		super.setup();
	}
	
	

	@Test
	public void testInteract2()throws Exception{
		startStep("Init test data");
	
		Course course = pageHelper.initCouse(10);
		String[] words;
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
		interactSection.clickInteract2StartButtn();
		
		startStep("Check that speaker text is highlighted");
		interactSection.checkThatSpeakerTextIsHighlighted();
		
		startStep("wait untile playback ends");
		
		
		
		
		
	}
	
	@After
	public void tearDown()throws Exception{
		super.tearDown();
	}

}
