package tests.misc;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import services.AudioService;

public class NonUiTests extends EdusoftBasicTest {
	

	@Before
	public void setup() throws Exception {
		super.setup();
	}



	@Test
	public void testAudio() throws Exception {
		
		AudioService audioService=new AudioService();
		audioService.sendSoundToVirtualMic(new File("files/audioFiles/800$.wav"),8000.0F);
		//8000 for recordings with SRI tool
//		System.out.println("Do not run");
	}
	
	@Test
	public void testFailTest()throws Exception{
		testResultService.addFailTest("aaaaa");
		testResultService.assertEquals("dog", "dog");
		testResultService.addFailTest("ccccc");
	}


	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
