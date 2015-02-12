package tests.edo.newux;

import org.junit.Test;

import Interfaces.TestCaseParams;

public class NewUXFooterTests extends BasicNewUxTest {

	@Test
	@TestCaseParams(testCaseID = { "18447" })
	public void testFooterContactUs() throws Exception {
		newUxHomePage.clickOnAboutEdusoft();
		String currentUrl = webDriver.getUrl();
		
		testResultService.assertEquals("http://www.edusoftlearning.com/", currentUrl,"about site is not displayed");
	}

}
