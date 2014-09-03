package tests.edoRegression;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import tests.misc.EdusoftWebTest;
import Objects.Course;
import Objects.Student;
import Objects.Teacher;

public class EraterRegressionTests extends EdusoftWebTest {

	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();

	TmsHomePage tmsHomePage;
	String writingId = null;
	String newWritingId = null;

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = pageHelper.getCourses();
		report.startLevel("delete all student assignments",
				Reporter.EnumReportLevel.CurrentPlace);
		String userId = dbService.getUserIdByUserName(
				configuration.getStudentUserName(),
				autoInstitution.getInstitutionId());
		eraterService.deleteStudentAssignments(userId);
		report.stopLevel();
		report.startLevel("Set institution to 'Teacher first' settings",
				Reporter.EnumReportLevel.CurrentPlace);
		eraterService.setEraterTeacherLast();
		report.stopLevel();
	}

	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainChrome()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }
	//
	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainFirefox()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }
	// @Test
	// public void
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgainIE()
	// throws Exception {
	// testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain(webDriver);
	// }
	@Test
	public void testSubmitTextAsStudentAndCheckFeedbackAsTeacherAndSendAgain()
			throws Exception {
		startStep("Create a student for the test");
		String StudentUserName = "student" + dbService.sig(6);
		pageHelper.addStudent(StudentUserName);
		startStep("Login to Edo");
		int courseId = 12;
		String textFile = "files/assayFiles/text24.txt";
		report.report("using file: " + textFile);
		// EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
		// testResultService);
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		//
		Student student = new Student();
		student.setUserName(StudentUserName);
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(StudentUserName,
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = pageHelper.loginAsStudent(student);

		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
		sleep(3);
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = courses.get(courseId).getCourseUnits().get(0)
				.getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
				.get(0).getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		String textStart = dbService.sig(8);
		edoHomePage.submitWritingAssignment(textFile, textService, textStart);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		// String textStart = textService.getFirstCharsFromCsv(10, textFile);
		writingId = eraterService.getWritingIdByUserIdAndTextStart(userId,
				textStart);
		writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();
		sleep(3);
		startStep("Check the Erater feedback as a student and send again");
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

		startStep("Login as teacher and send feedback to the student");
		webDriver.deleteCookiesAndRefresh();
		Teacher teacher = new Teacher(
				configuration.getProperty("teacher.username"),
				configuration.getProperty("teacher.password"));
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		// edoHomePage = edoLoginPage.login(teacher);
		// edoHomePage.waitForPageToLoad();

		pageHelper.loginAsTeacher();
		webDriver.closeAlertByAccept();
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), newText);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();

		// tmsHomePage.clickOnAssignment(courseUnit);
		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
		tmsHomePage.clickOnAssignmentSummary();
		sleep(2);
		tmsHomePage.clickOnRateAssignmentButton();
		int rating = 1;
		tmsHomePage.rateAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		Thread.sleep(2000);
		tmsHomePage.sendFeedback();

		Thread.sleep(2000);

		startStep("Login again as student and check the feedback from the teacher");
		webDriver.quitBrowser();
		webDriver.init(testResultService);
		webDriver.openUrl(getSutAndSubDomain());
		// edoLoginPage.login(student);
		pageHelper.loginAsStudent(student);
		sleep(2);

		edoHomePage.clickOnMyAssignments();

		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		sleep(2);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
		// edoHomePage.switchToAssignmentsFrame();
		edoHomePage.checkRatingFromTeacher(rating);
		// edoHomePage.clickOnFeedbackMoreDetails();

	}

	@Test
	public void testSaveAssignmentAndSend() throws Exception {

		startStep("init test data");
		Student student = new Student();
		student.setUserName(configuration.getStudentUserName());
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId()));

		startStep("Login as student and enter some text");
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		
		EdoHomePage edoHomePage = pageHelper.loginAsStudent(student);
		sleep(2);

		String addedText = textService.getTextFromFile(
				"files/assayFiles/text2.txt", Charset.defaultCharset());

		edoHomePage.addWritingAssignmentWithoutSubmitting(courses.get(3),
				addedText, textService);

		startStep("Save and leave the writing assignment");
		edoHomePage.clickToSaveAssignment();
		edoHomePage.clickOnLastComponent(1);
		Thread.sleep(3000);

		startStep("Move back to the writing assignment and check the text");
		edoHomePage.clickOnNextComponent(1);
		Thread.sleep(3000);
		// String AssignmentText = edoHomePage.getAssignmentTextFromEditor();
		// Assert.assertEquals("Saved text and current text are not equal",
		// AssignmentText, addedText);

		startStep("Send the assignment");
		edoHomePage.clickOnSubmitAssignment();

		startStep("Check the assignment in the DB");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		Assert.assertNotNull("validate writing id in DB",
				eraterService.getWritingIdByUserId(userId));

	}

	@Test
	public void testAddTeacherCommentToStudentAssignment() throws Exception {
		startStep("Create a student for the test");
		String StudentUserName = "student" + dbService.sig(6);
		pageHelper.addStudent(StudentUserName);

		startStep("Login to Edo");
		int courseId = 3;
		String textFile = "files/assayFiles/text24.txt";
		String commentID = "0_0";
		report.report("using file: " + textFile);
		// EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		//
		// Student student = new Student();
		// student.setUserName(configuration.getStudentUserName());
		// student.setPassword(configuration.getStudentPassword());
		// student.setId(dbService.getUserIdByUserName(student.getUserName(),
		// autoInstitution.getInstitutionId()));
		// EdoHomePage edoHomePage = edoLoginPage.login(student);
		Student student = new Student();
		student.setUserName(StudentUserName);
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(StudentUserName,
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = pageHelper.loginAsStudent(student);

		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
		sleep(2);
		edoHomePage.clickOnCourses();
		sleep(5);
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		String courseUnit = courses.get(courseId).getCourseUnits().get(0)
				.getName();
		edoHomePage.clickOnCourseUnit(courseUnit);
		String unitComponent = courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName();
		edoHomePage.clickOntUnitComponent(unitComponent, "Practice");
		Thread.sleep(5000);
		int unitStage = Integer.valueOf(courses.get(courseId).getCourseUnits()
				.get(0).getUnitComponent().get(0).getStageNumber());
		edoHomePage.ClickOnComponentsStage(unitStage);
		sleep(3);
		String textStart = dbService.sig(8);
		edoHomePage.submitWritingAssignment(textFile, textService, textStart);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		// String textStart = textService.getFirstCharsFromCsv(10, textFile);
		writingId = eraterService.getWritingIdByUserIdAndTextStart(userId,
				textStart);
		writingIdForDelete.add(writingId);
		report.report("writing id is: " + writingId);
		eraterService.compareJsonAndXmlByWritingId(writingId);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();

		startStep("Check the Erater feedback as a student and send again");
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

		startStep("Login as teacher and send feedback to the student");
		webDriver.deleteCookiesAndRefresh();
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		// edoHomePage = edoLoginPage.login(teacher);
		edoHomePage = pageHelper.loginAsTeacher();
		edoHomePage.waitForPageToLoad();
		tmsHomePage = edoHomePage.openTeachersCorner();
		newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), textStart);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();

		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);
		// ********************
		startStep("Check that the Add button is disabled when no comment is selected");
		tmsHomePage.clickOnXFeedback();
		// webDriver.switchToFrame("cboxIframe");
		tmsHomePage.checkAddCommentButtonStatus(true);

		startStep("Click the Add buttom, select a plave and add a comment");
		tmsHomePage.ClickAddCommentButton();

		// click on position to add commentR
		tmsHomePage.clickOnTextArea(20, 20);
		String commentText = "comment" + dbService.sig(5);
		tmsHomePage.enterTeacherCommentText(commentText);
		tmsHomePage.clickAddCommentDoneButton();

		// ****************************
		// startStep("Send the feedback to the student");
		// tmsHomePage.clickOnTeacherFeedbackContinueButton();
		// tmsHomePage.clickOnRateAssignmentButton();
		// int rating = 1;
		// tmsHomePage.rateAssignment(rating);
		// Thread.sleep(2000);
		// tmsHomePage.clickOnApproveAssignmentButton();
		// sleep(4);
		// tmsHomePage.sendFeedback();
		// sleep(4);
		sendTeacherFeedback();

		startStep("Login as student and check that the commnet is not displayed");
		webDriver.quitBrowser();
		webDriver.init(testResultService);
		pageHelper.loginAsStudent();
		sleep(5);
		webDriver.printScreen("after student login");
		edoHomePage.clickOnMyAssignments();
		sleep(3);
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
		edoHomePage.clickOnFeedbackMoreDetails();
		sleep(2);
		// edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// webDriver.switchToFrame(webDriver.waitForElement(
		// "//iframe[@class='cboxIframe']", ByTypes.xpath));

		edoHomePage.checkForTeacherComment("t0_1_0", commentText);

	}

	@After
	public void tearDown() throws Exception {
		report.report("Delete writings");
		for (int i = 0; i < writingIdForDelete.size(); i++) {
			eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		}
		super.tearDown();

	}

	public void sendTeacherFeedback() throws Exception {
		startStep("Send the feedback to the student");
		tmsHomePage.clickOnTeacherFeedbackContinueButton();
		sleep(2);
		tmsHomePage.clickOnRateAssignmentButton();
		int rating = 1;
		tmsHomePage.rateAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		sleep(4);
		tmsHomePage.sendFeedback();
		sleep(4);
		webDriver.printScreen("After clicking send to all");
		eraterService.checkWritingIsReviewed(newWritingId);

	}

}
