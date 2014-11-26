package tests.misc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jsystem.framework.RunProperties;
import jsystem.framework.scenario.RunningProperties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.AutoParams;
import services.AudioService;

public class NonUiTests extends EdusoftBasicTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testAudio() throws Exception {

		AudioService audioService = new AudioService();
		audioService.sendSoundToVirtualMic(new File(
				"files/audioFiles/A1SHHA_3.wav"), 16000);
		// 8000 for recordings with SRI tool
		// System.out.println("Do not run");
	}

	// @Test
	public void testFailTest() throws Exception {
		// testResultService.addFailTest("aaaaa");

		String str1 = "dog";
		String str2 = "dog";
		testResultService.assertEquals(str1, str2);
		// testResultService.assertEquals("dog", "cat");
		// testResultService.addFailTest("ccccc");

	}

	@Test
	public void updateSlaveStatus() throws Exception, IOException {
		String text = textService.getTextForText(50);
		System.out.println(text);
	}

	@Test
	public void getLastProgress() throws Exception {
		try {
		String[]str=	dbService.getClassAndCourseWithLastProgress("autoTeacher",
					autoInstitution.getInstitutionId());
		System.out.println(str[0]);
		System.out.println(str[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
