package tests.misc;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import Interfaces.TestCaseParams;
import pageObjects.EdoLoginPage;
import services.MyJRunner;
import services.MyTestRunner;

public class NonUiTests extends EdusoftBasicTest {
	

	@Before
	public void setup() throws Exception {
		super.setup();
	}



	@Test
	public void testAudio() throws Exception {
		audioService.sendSoundToVirtualMic(new File("files/audioFiles/207838.wav"));
	
//		System.out.println("Do not run");
	}


	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
