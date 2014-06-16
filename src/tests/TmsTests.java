package tests;

import jsystem.framework.report.Reporter.EnumReportLevel;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TeacherDetailsPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import Objects.Student;
import Objects.Teacher;
import Objects.UserObject;

public class TmsTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testLoginToTms() throws Exception {

	}

	@Test
	public void testLoginToEdoAsTeacher() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setUserName(config.getProperty("teacher.username"));
		teacher.setPassword(config.getProperty("teacher.password"));

		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.login(teacher);
	}

	@Test
	public void RegisterTeacherAndLogin() throws Exception {
		report.startLevel("Open TMS and login as TMS Admin",
				EnumReportLevel.CurrentPlace);
		String teacherName = "teacher" + dbService.sig(6);
		String teacherPassword = "12345";
		
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(config.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(config.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(config.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		report.startLevel("Go to teachers section and select the institute",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnTeachers();
		String institutionId = config.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId);
		report.stopLevel();

		report.startLevel("Enter new teacher details",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.swithchToMainFrame();
		TeacherDetailsPage teacherDetailsPage = tmsHomePage
				.clickOnAddNewTeacher();

		teacherDetailsPage.typeTeacherFirstName(teacherName);
		teacherDetailsPage.typeTeacherLastName(teacherName);
		teacherDetailsPage.typeTeacherUserName(teacherName);
		teacherDetailsPage.typeTeacherPassword(teacherPassword);
		teacherDetailsPage.addClass();
		teacherDetailsPage.clickOnSubmit();

		report.stopLevel();

		report.startLevel("Logout from TMS and login as the new teacher",
				EnumReportLevel.CurrentPlace);
		webDriver.quitBrowser();
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Teacher teacher = new Teacher(teacherName, teacherPassword);
		EdoHomePage homePage = edoLoginPage.login(teacher);
		homePage.waitForPageToLoad();
		report.stopLevel();

	}

	// @Test
	// public void RegisterStudentAndLogin() throws Exception {
	// RegisterStudentAndLogin(chromeWebDriver);
	// }

	// public void RegisterStudentAndLoginIE() throws Exception {
	// RegisterStudentAndLogin(ieWebDriver);
	// }
	// @Test
	// public void RegisterStudentAndLoginFirefox() throws Exception {
	// RegisterStudentAndLogin(firefoxDriver);
	// }
	@Test
	public void RegisterStudentAndLogin() throws Exception {
		report.startLevel("Open TMS and login as TMS Admin",
				EnumReportLevel.CurrentPlace);

		String studentName = "student" + dbService.sig(6);
		String studentPassword = "12345";
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(config.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(config.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(config.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		report.startLevel("Go to students section and select the institute",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnStudents();
		String institutionId = config.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId);
		Thread.sleep(1000);
		tmsHomePage.selectClass(config.getProperty("classname"));

		report.stopLevel();

		report.startLevel("Enter new student details",
				EnumReportLevel.CurrentPlace);

		if(webDriver instanceof IEWebDriver){
			Thread.sleep(1000);
		}
		
		tmsHomePage.enterStudentDetails(studentName);
		String userId = dbService.getUserIdByUserName(studentName);
		tmsHomePage.enterStudentPassword(userId, studentPassword);

		report.stopLevel();
		report.startLevel("Login as student", EnumReportLevel.CurrentPlace);
		webDriver.closeBrowser();
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Student student = new Student();
		student.setUserName(studentName);
		student.setPassword(studentPassword);
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		edoHomePage.waitForPageToLoad();

		report.stopLevel();

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
