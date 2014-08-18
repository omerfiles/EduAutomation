package tests.misc;

import java.io.File;

import jsystem.framework.RunProperties;
import jsystem.framework.scenario.RunningProperties;

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
		audioService.sendSoundToVirtualMic(new File("files/audioFiles/imFromChine_S1SVAA_4.wav"),0);
		//8000 for recordings with SRI tool
//		System.out.println("Do not run");
	}
	
	@Test
	public void testFailTest()throws Exception{
//		testResultService.addFailTest("aaaaa");
		
		String str1="dog";
		String str2="dog";
		testResultService.assertEquals(str1,str2);
//		testResultService.assertEquals("dog", "cat");
//		testResultService.addFailTest("ccccc");
		
		
	}


	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
