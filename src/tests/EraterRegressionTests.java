package tests;

import java.util.List;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import Objects.Course;
import Objects.Student;
import Objects.Teacher;

public class EraterRegressionTests extends EdusoftWebTest {

	List<Course> courses = null;

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = eraterService.loadCoursedDetailsFromCsv();
	}

	@Test
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain()
			throws Exception {
		testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(chromeWebDriver);
	}

	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(
			GenericWebDriver webDriver) throws Exception {
		report.startLevel("Login to Edo", EnumReportLevel.CurrentPlace);
		webDriver.init();
		String textFile = "files/assayFiles/text1.txt";
		report.report("using file: " + textFile);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());

		Student student = new Student();
		student.setUserName(config.getProperty("student.user.name"));
		student.setPassword(config.getProperty("student.user.password"));
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		
		// webDriver.closeAlertByAccept();
		report.stopLevel();

		// report.startLevel("Open home page and start a writing drill",
		// EnumReportLevel.CurrentPlace);
		String courseName = "Basic 3 2012";
		// edoHomePage.clickOnCourseByName(courseName);
		// edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = "Getting Help";
		// edoHomePage.clickOnCourseUnit(courseUnit);
		// String unitComponent = "Tickets, Please!";
		// edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		// int unitStage = 7;
		// edoHomePage.ClickOnComponentsStage(unitStage);
		// Thread.sleep(10000);
		// edoHomePage.submitWritingAssignment(textFile, textService);
		// // System.out.println("sleeping for 60 seconds");
		// // Thread.sleep(60000);
		//
		// report.startLevel("start checking the xml and json",
		// EnumReportLevel.CurrentPlace);
		// String userId = dbService.getUserIdByUserName(student.getUserName());
		// String textStart = textService.getFirstCharsFromCsv(10, textFile);
		// String writingId = eraterService.getWritingIdByUserIdAndTextStart(
		// userId, textStart);
		// report.report("using file: " + textFile);
		// eraterService.compareJsonAndXmlByWritingId(writingId);
		// edoHomePage.waitForPageToLoad();
		// edoHomePage.clickOnCourses();
		// String courseName = null;
		// report.stopLevel();
		//
		// report.startLevel("Login as teacher and send feedback to the student",
		// EnumReportLevel.CurrentPlace);
		// webDriver.deleteCookiesAndRefresh();
		Teacher teacher = new Teacher(config.getProperty("teacher.username"),
				config.getProperty("teacher.password"));
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoHomePage = edoLoginPage.login(teacher);
		edoHomePage.waitForPageToLoad();
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		tmsHomePage.clickOnWritingAssignments();

		// tmsHomePage.clickOnAssignment(courseUnit);
		tmsHomePage.clickOnStudentAssignment(student.getName(), courseName);

		report.stopLevel();

	}

	@Test
	public void testSaveAssignmentAndSendChrome() throws Exception {
		testSaveAssignmentAndSend(chromeWebDriver);

	}

	public void testSaveAssignmentAndSend(GenericWebDriver webDriver)
			throws Exception {

		report.startLevel("Test setup", Reporter.CurrentPlace);
		webDriver.init();
		Student student = new Student();
		student.setUserName(config.getStudentUserName());
		student.setPassword(config.getStudentPassword());

		report.stopLevel();

		report.startLevel("Login as student and enter some text",
				Reporter.CurrentPlace);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		edoHomePage.addWritingAssignmentWithoutSubmitting(courses.get(0), "files/assayFiles/text1.txt");
		edoHomePage.clickToSaveAssignment();
		report.stopLevel();

		report.startLevel("Save and leave the writing assignment",
				Reporter.CurrentPlace);
		edoHomePage.clickToSaveAssignment();
		edoHomePage.clickOnLastComponent(1);
		report.stopLevel();

		report.startLevel(
				"Move back to the writing assignment and check the text",
				Reporter.CurrentPlace);
		edoHomePage.clickOnNextComponent(1);
		
		report.stopLevel();

		report.startLevel("Send the assignment", Reporter.CurrentPlace);

		report.stopLevel();

		report.startLevel("Send the assignment", Reporter.CurrentPlace);

		report.stopLevel();

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
