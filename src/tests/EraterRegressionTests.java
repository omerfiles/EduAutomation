package tests;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jsystem.framework.analyzer.Analyzer;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.Assert;

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
	List<String>writingIdForDelete=new ArrayList<String>();

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = edoService.getCourses();
		report.startLevel("delete all student assignments",
				Reporter.EnumReportLevel.CurrentPlace);
		String userId = dbService.getUserIdByUserName(config
				.getStudentUserName());
		eraterService.deleteStudentAssignments(userId);
		report.stopLevel();
		report.startLevel("Set institution to 'Teacher first' settings",
				Reporter.EnumReportLevel.CurrentPlace);
		eraterService.setEraterTeacherLast();
		report.stopLevel();
	}

	@Test
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainChrome()
			throws Exception {
		testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	}

	@Test
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainFirefox()
			throws Exception {
		testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	}
	@Test
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainIE()
			throws Exception {
		testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	}

	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(
			GenericWebDriver webDriver) throws Exception {
		report.startLevel("Login to Edo", EnumReportLevel.CurrentPlace);
		int courseId=2;
		webDriver.init();
		String textFile = "files/assayFiles/text24.txt";
		report.report("using file: " + textFile);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());

		Student student = new Student();
		student.setUserName(config.getStudentUserName());
		student.setPassword(config.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName()));
		EdoHomePage edoHomePage = edoLoginPage.login(student);

		report.stopLevel();

		report.startLevel("Open home page and start a writing drill",
				EnumReportLevel.CurrentPlace);
		String courseName = courses.get(courseId).getName();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit =courses.get(courseId).getCourseUnits().get(0).getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0).getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage =Integer.valueOf(courses.get(courseId).getCourseUnits().get(0).getUnitComponent().get(0).getStageNumber()) ;
		edoHomePage.ClickOnComponentsStage(unitStage);

		edoHomePage.submitWritingAssignment(textFile, textService);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		report.startLevel("start checking the xml and json",
				EnumReportLevel.CurrentPlace);
		String userId = dbService.getUserIdByUserName(student.getUserName());
		String textStart = textService.getFirstCharsFromCsv(10, textFile);
		String writingId = eraterService.getWritingIdByUserIdAndTextStart(
				userId, textStart);
		writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();
		report.stopLevel();

		report.startLevel(
				"Check the Erater feedback as a student and send again",
				EnumReportLevel.CurrentPlace);
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedbackAndTryAgsin();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnFeedbackMoreDetails();
		String newText = "this is new text";
		edoHomePage.editFeedbackAssignmentText(newText);

		edoHomePage.clickOnFeedbackSubmitBtn();

		report.stopLevel();

		report.startLevel("Login as teacher and send feedback to the student",
				EnumReportLevel.CurrentPlace);
		webDriver.deleteCookiesAndRefresh();
		Teacher teacher = new Teacher(config.getProperty("teacher.username"),
				config.getProperty("teacher.password"));
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoHomePage = edoLoginPage.login(teacher);
		edoHomePage.waitForPageToLoad();
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		String newWritingId=eraterService.getWritingIdByUserIdAndTextStart(student.getId(), newText);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();

		// tmsHomePage.clickOnAssignment(courseUnit);
		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
		tmsHomePage.clickOnAssignmentSummary();
		tmsHomePage.clickOnRateAssignmentButton();
		int rating=1;
		tmsHomePage.reteAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		Thread.sleep(2000);
		tmsHomePage.sendFeedback();
		Thread.sleep(2000);

		report.stopLevel();
		
		report.startLevel("Login again as student and check the feedback from the teacher",EnumReportLevel.CurrentPlace);
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.openUrl(getSutAndSubDomain());
		edoLoginPage.login(student);
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
//		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.checkRatingFromTeacher(rating);
//		edoHomePage.clickOnFeedbackMoreDetails();
	
		
		report.stopLevel();

	}

	@Test
	public void testSaveAssignmentAndSendChrome() throws Exception {
		testSaveAssignmentAndSend(webDriver);

	}
	@Test
	public void testSaveAssignmentAndSendFirefox() throws Exception {
		testSaveAssignmentAndSend(webDriver);

	}
	@Test
	public void testSaveAssignmentAndSendIE() throws Exception {
		testSaveAssignmentAndSend(webDriver);

	}

	public void testSaveAssignmentAndSend(GenericWebDriver webDriver)
			throws Exception {

		report.startLevel("Test setup", Reporter.CurrentPlace);
		webDriver.init();
		Student student = new Student();
		student.setUserName(config.getStudentUserName());
		student.setPassword(config.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName()));

		report.stopLevel();

		report.startLevel("Login as student and enter some text",
				Reporter.CurrentPlace);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		EdoHomePage edoHomePage = edoLoginPage.login(student);

		String addedText = textService.getTextFromFile(
				"files/assayFiles/text2.txt", Charset.defaultCharset());

		edoHomePage.addWritingAssignmentWithoutSubmitting(courses.get(3),
				addedText, textService);
		report.stopLevel();

		report.startLevel("Save and leave the writing assignment",
				Reporter.CurrentPlace);
		edoHomePage.clickToSaveAssignment();
		edoHomePage.clickOnLastComponent(1);
		Thread.sleep(3000);
		report.stopLevel();

		report.startLevel(
				"Move back to the writing assignment and check the text",
				Reporter.CurrentPlace);
		edoHomePage.clickOnNextComponent(1);
		Thread.sleep(3000);
		String AssignmentText = edoHomePage.getAssignmentTextFromEditor();
//		Assert.assertEquals("Saved text and current text are not equal",
//				AssignmentText, addedText);

		report.stopLevel();

		report.startLevel("Send the assignment", Reporter.CurrentPlace);
		edoHomePage.clickOnSubmitAssignment();
		report.stopLevel();

		report.startLevel("Check the assignment in the DB",
				Reporter.CurrentPlace);
		String userId = dbService.getUserIdByUserName(student.getUserName());
		Assert.assertNotNull("validate writing id in DB",
				eraterService.getWritingIdByUserId(userId));
		report.stopLevel();

	}

	@After
	public void tearDown() throws Exception {
		report.report("Delete writings");
		for(int i=0;i<writingIdForDelete.size();i++){
			eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		}
		super.tearDown();
		
	}

}
