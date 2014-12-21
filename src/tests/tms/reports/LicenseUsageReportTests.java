package tests.tms.reports;

import org.junit.Before;
import org.junit.Test;

import Enums.ByTypes;
import Enums.UserType;
import Interfaces.TestCaseParams;
import pageObjects.EdoHomePage;
import pageObjects.tms.ReportPage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;

public class LicenseUsageReportTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	
	@Test
	@TestCaseParams(testCaseID = { "16157" })
	public void testLicenseUsageReportAsSchoolAdmin() throws Exception{
		testLicenseUsageReportAsTMSDomain_16168(UserType.SchoolAdmin);
	}
	@Test
	@TestCaseParams(testCaseID = { "16156" })
	public void testLicenseUsageReportAsTeacher() throws Exception{
		testLicenseUsageReportAsTMSDomain_16168(UserType.Teahcer);
	}
	@Test
	@TestCaseParams(testCaseID = { "16169" })
	public void testLicenseUsageReportAsSuperVisor() throws Exception{
		testLicenseUsageReportAsTMSDomain_16168(UserType.Supervisor);
	}
	@Test
	@TestCaseParams(testCaseID = { "16168" })
	public void testLicenseUsageReportAsTMSadmin() throws Exception{
		testLicenseUsageReportAsTMSDomain_16168(UserType.TMSAdmin);
	}

	public void testLicenseUsageReportAsTMSDomain_16168(UserType userType)
			throws Exception {
		String packageName = "FD-A3/6m_7m";
		String classForTest="class1";
		String studentFirstName = configuration.getProperty("student");

		// Login as TMSDOMAIN
		report.startLevel("Login as " + userType.toString());

		TmsHomePage tmsHomePage = null;
		EdoHomePage edoHomePage=null;

		switch (userType) {

		case TMSAdmin:
			tmsHomePage = pageHelper.loginToTmsAsAdmin();
			break;
		case Supervisor:
			edoHomePage = pageHelper.loginAsSupervisor();
			edoHomePage.waitForPageToLoad();
			tmsHomePage =(TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		case Teahcer:
			edoHomePage = pageHelper.loginAsTeacher();
			edoHomePage.waitForPageToLoad();
			tmsHomePage =(TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		case SchoolAdmin:
			edoHomePage = pageHelper.loginAsSchoolAdmin();
			edoHomePage.waitForPageToLoad();
			tmsHomePage =(TmsHomePage) edoHomePage.openTeachersCorner();
			break;
		}
		

		// Open reports
		report.startLevel("Click on reports");
		tmsHomePage.cliclOnReports();
		// Select Course reports
		report.startLevel("Click on course reports");
		tmsHomePage.clickOnCourseReports();
		// Select License Usage reports
		report.startLevel("Click on license usage");
		tmsHomePage.selectCourseReport("License Usage");
		sleep(1);
		if(userType==UserType.TMSAdmin){
			report.startLevel("Select institution");
			tmsHomePage.selectInstitute(autoInstitution.getInstitutionName(),
					autoInstitution.getInstitutionId(), false, false);
			sleep(1);
		}
		if(userType==UserType.Supervisor){
			report.startLevel("Select teacher");
			tmsHomePage.selectTeacher(pageHelper.getTeacher().getUserName());
			sleep(1);
		}
		
		report.startLevel("Select class");
		tmsHomePage.selectClass(classForTest, false,
				false);
		sleep(2);
		report.startLevel("Select package name");
		tmsHomePage.selectPackageByName(packageName);
		sleep(2);
		tmsHomePage.clickOnGo();
		sleep(5);
//		webDriver.printScreen();
		tmsHomePage.switchToReportFrame();

		ReportPage reportPage = new ReportPage(webDriver, testResultService);
		String packageNameFromSammary = reportPage.getSammaryPackageName();
		String classNameFromSammary = reportPage.getSammaryClassName();

		testResultService.assertEquals(packageName, packageNameFromSammary,
				"Package name not found in report sammary");

		testResultService.assertEquals(classForTest,
				classNameFromSammary, "class name not found in report sammary");

		report.startLevel("Check for specific student details");
		String studentFirstAndLastName = reportPage
				.getStduentNameFromLicenseReport(studentFirstName);
		testResultService.assertEquals(studentFirstName + " "
				+ studentFirstName, studentFirstAndLastName,
				"Student name not found");

	}
}
