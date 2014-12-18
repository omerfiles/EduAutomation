package tests.tms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import Interfaces.TestCaseParams;
import tests.misc.EdusoftWebTest;

public class AccessLicenseTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	@TestCaseParams(testCaseID = { "16830" })
	public void deactivateStudent() throws Exception {
		String studentUserName = "Stud" + dbService.sig(6);
		report.report("Student user name is: " + studentUserName);
		System.out.println(studentUserName);
		String schoolClass = "class005";
		pageHelper.addStudent(studentUserName, schoolClass);

		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();

		report.startLevel("Open licences");
		tmsHomePage.clickOnLicenses();
		sleep(2);
		report.startLevel("Select institution, class and student");
		tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(),
				autoInstitution.getInstitutionId(), false, true);

		tmsHomePage.selectClass(schoolClass, true, false);
		sleep(2);
		tmsHomePage.selecStudent(studentUserName);
		tmsHomePage.clickOnGo();
		tmsHomePage.swithchToMainFrame();
		report.startLevel("Deactivate the student");
		tmsHomePage.deactivateStudent();
		webDriver.switchToMainWindow();
		tmsHomePage.swithchToMainFrame();
		
		tmsHomePage.clickOnSave();

		report.startLevel("Try to login with the deactivated student");
		webDriver.deleteCookiesAndRefresh();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.typeUserNameAndPass(studentUserName, "12345");
		edoLoginPage.submitLogin();
		String popupText = edoLoginPage.getPopupText();
		testResultService
				.assertEquals("Your access license is no longer active. Please contact your administrator for activation.", popupText, "popup text not found");

	}

	@After
	public void tearDowb() throws Exception {
		super.tearDown();
	}

}
