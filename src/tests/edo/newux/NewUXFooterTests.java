package tests.edo.newux;

import org.junit.Test;

import Interfaces.TestCaseParams;

public class NewUXFooterTests extends BasicNewUxTest {

	@Test
	@TestCaseParams(testCaseID = { "18447" })
	public void testFooterContactUs() throws Exception {
		newUxHomePage.clickOnAboutEdusoft();
		webDriver.switchToNextTab();
		String currentUrl = webDriver.getUrl();
		
		testResultService.assertEquals("http://www.edusoftlearning.com/", currentUrl,"about site is not displayed");
	}
	
	@Test
	@TestCaseParams(testCaseID = { "18443" })
	public void testFooterPrivacyStatement()throws Exception{
		newUxHomePage.clickOnPrivacyStatement();
		String title=newUxHomePage.getOcModalTitleText();
		testResultService.assertEquals("Privacy Statement", title);
		
	}
	
	@Test
	@TestCaseParams(testCaseID = { "18448" })
	public void testFooterLegalNoticePopup()throws Exception{
		newUxHomePage.clickOnLegalNotice();String title=newUxHomePage.getOcModalTitleText();
		testResultService.assertEquals("Legal Notices", title);
	}

}
