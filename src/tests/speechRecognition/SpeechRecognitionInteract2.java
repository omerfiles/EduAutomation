package tests.speechRecognition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tests.misc.EdusoftWebTest;

public class SpeechRecognitionInteract2 extends EdusoftWebTest {
	
	@Before
	public void setup()throws Exception{
		super.setup();
	}
	
	
	//Test case 13305
	//SR: Interact1 - behavior after 3 times of silence
	@Test
	public void testInteract1BehaviorAfter3TimesOfSilence()throws Exception{
		startStep("Init test data");
		
		startStep("Open course unit");
		
		startStep("Click on Interact1 - right arrow -click the start button ");
		
		startStep("wait untile playback ends");
		
		startStep("Check that the Instruction and Recording indication changes");
		
		startStep("Check that Retry and Start button are disabled");
		
		startStep("Click the Retry button");
		
		
		
	}
	
	@After
	public void tearDown()throws Exception{
		super.tearDown();
	}

}
