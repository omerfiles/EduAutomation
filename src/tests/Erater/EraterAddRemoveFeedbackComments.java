package tests.Erater;

import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import Enums.ByTypes;
import Objects.Course;
import Objects.Student;
import Objects.Teacher;
import tests.misc.EdusoftWebTest;

public class EraterAddRemoveFeedbackComments extends EdusoftWebTest {

	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();

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

	@Test
	public void testCheckRemoveButtonEnabledDisabled() throws Exception {
		startStep("Login to Edo");
		int courseId = 3;
		String textFile = "files/assayFiles/text24.txt";
		String commentID = "0_0";
		report.report("using file: " + textFile);
		// EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		// edoLoginPage.OpenPage(getSutAndSubDomain());
		//
		Student student = new Student();
		student.setUserName(configuration.getStudentUserName());
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId()));
		// EdoHomePage edoHomePage = edoLoginPage.login(student);
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();

		startStep("Open home page and start a writing drill");
		String courseName = courses.get(courseId).getName();
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
		edoHomePage.submitWritingAssignment(textFile, textService);
		// System.out.println("sleeping for 60 seconds");
		// Thread.sleep(60000);

		startStep("start checking the xml and json");
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		String textStart = textService.getFirstCharsFromCsv(10, textFile);
		String writingId = eraterService.getWritingIdByUserIdAndTextStart(
				userId, textStart);
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
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		String newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
				student.getId(), newText);
		eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();

		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);

		startStep("Check that the Remove button is disabled when no comment is selected");
		tmsHomePage.clickOnXFeedback();
		tmsHomePage.checkRemoveCommentButtonStatus(false, true);

		startStep("Select a comment and check the remove button");
		String commentText = tmsHomePage.selectFeedbackComment(commentID);
		tmsHomePage.checkRemoveCommentButtonStatus(true, false);

		startStep("click the remove button and check the texts");
		tmsHomePage.clickTheRemoveCommentButton();
		tmsHomePage.checkIfCommentedTextIsInderlined(commentID, false);
		tmsHomePage.checkCommentTitle(false);

		startStep("Send the feedback to the student");
		tmsHomePage.clickOnTeacherFeedbackContinueButton();
		tmsHomePage.clickOnRateAssignmentButton();
		int rating = 1;
		tmsHomePage.rateAssignment(rating);
		Thread.sleep(2000);
		tmsHomePage.clickOnApproveAssignmentButton();
		Thread.sleep(2000);
		tmsHomePage.sendFeedback();
		Thread.sleep(2000);

		startStep("Login as student and check that the commnet is not displayed");
		webDriver.quitBrowser();
		webDriver.init(testResultService);
		pageHelper.loginAsStudent();
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		// edoHomePage.clickToViewAssignment(courseName);
		edoHomePage.clickOnSeeFeedback();
		edoHomePage.switchToAssignmentsFrame();
		Thread.sleep(3000);
		edoHomePage.clickOnFeedbackMoreDetails();
//		edoHomePage.clickOnWritingAssignmentsTab(courseName);
		edoHomePage.checkAsStudentFeedbackComment(commentID, false,commentText);
		
	}
	
	

	@After
	public void tearDown() throws Exception {
		report.report("Delete writings");
		for (int i = 0; i < writingIdForDelete.size(); i++) {
			eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		}
		super.tearDown();
	}

}
