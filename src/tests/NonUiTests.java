package tests;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoLoginPage;

public class NonUiTests extends EdusoftBasicTest {
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void failedTest() {
		this.testCaseId = "2121654";
		Assert.assertEquals("1", "0");
	}

	@Test
	public void testAudio() throws Exception {
//		audioService.sendSoundToVirtualMic(new File("files/audioFiles/207838.wav"));
	

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
