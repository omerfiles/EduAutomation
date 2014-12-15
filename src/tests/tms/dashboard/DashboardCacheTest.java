package tests.tms.dashboard;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import drivers.ChromeWebDriver;
import Interfaces.TestCaseParams;
import Objects.Course;
import Objects.Student;

public class DashboardCacheTest extends DashboardBasicTest {

	@Test
	@TestCaseParams(testCaseID = { "17987" })
	public void TestDataIsSavedToCacheAndLoaded() throws Exception {

		startStep("Set cheche in webConfig to be 3 minutes");
		// TODO TBD

		startStep("Login and teacher and check the data");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		String className = "classForCacheTest";
		String courseName = "1 unit 1 component";
		String studentName = "Stud"+dbService.sig(4);
		
		int cacheInterval=5;
		
		pageHelper.addStudent(studentName, className);

		Course course = pageHelper.getCourses().get(26);

		String oldScore = getAvgScoresByClassIdAndCourseId(className,
				courseName).get(0)[0];
		sleep(4);
		dashboardPage.ClickOnBar();
		dashboardPage.selectClassInDashBoard(className);
	
		dashboardPage.selectCourseInDashboard(courseName);
		dashboardPage.clickOnDashboardGoButton();
		sleep(5);
		startStep("Check the test score");
		// String []testResults=dashboardPage.getClassTestScoreResults();
		// System.out.println(textService.printStringArray(testResults));

		startStep("open new browser and complete the test");
		webDriver.closeBrowser();
		webDriver.switchToMainWindow();
		webDriver.deleteCookiesAndRefresh();
		webDriver.openUrl(getSutAndSubDomain());
		Student student = new Student();
		student.setUserName(studentName);
		student.setPassword("12345");

		edoHomePage = pageHelper.loginAsStudent(student);
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.clickOnCourseUnit(course.getCourseUnits().get(0).getName());
		edoHomePage.clickOntUnitComponent(course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getName(), "Test");
		edoHomePage.clickOnStartTest();

//		int rand = dbService.getRandonNumber(1, 100);
//		System.out.println("Randon number is: " + rand);

		boolean[]answers = pageHelper.randomizeCorrectAndIncorrectAnswers(4);
		
		if (answers[0] == true) {
			edoHomePage.selectTestRadioAnswer("q1a2");
		} else {
			edoHomePage.selectTestRadioAnswer("q1a3");
		}
		// second question

		edoHomePage.clickOnNextComponent(1);
		if (answers[1] == true) {
			edoHomePage.selectTestRadioAnswer("q1a1");
		} else {
			edoHomePage.selectTestRadioAnswer("q1a2");
		}

		edoHomePage.clickOnNextComponent(1);
		if (answers[2] == true) {
			edoHomePage.dragAnswerToElement("54",
					edoHomePage.getDropQuestionByQnum("1"));
		} else {
			edoHomePage.dragAnswerToElement("93",
					edoHomePage.getDropQuestionByQnum("1"));
		}

		edoHomePage.clickOnNextComponent(1);
		if (answers[3] == true) {
			edoHomePage.selectTestRadioAnswer("q1a1");
		} else {
			edoHomePage.selectTestRadioAnswer("q1a2");
		}

		edoHomePage.clickOnSubmitTest();

		String score = edoHomePage.getTestScore();
		System.out.println("Test score was: " + score);

		startStep("Switch to the teacher browser and check that data was not updated in the chart");
		webDriver.deleteCookiesAndRefresh();
		pageHelper.loginAsTeacher();
		dashboardPage = (DashboardPage) edoHomePage.clickOnTeachersCorner(true);

		dashboardPage.ClickOnBar();
//		sleep(2);
//		dashboardPage.selectClassInDashBoard(className);
//
//		dashboardPage.selectCourseInDashboard(courseName);
//		dashboardPage.clickOnDashboardGoButton();
		String actualUnitScore = dashboardPage
				.getAvgScorePerUnitClassTestScore(0);
//		testResultService.assertEquals(oldScore, actualUnitScore);
		startStep("Wait until cache is refreshed");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		DateTime dt = formatter.parseDateTime(dashboardPage.getDashboardLastUpdateDateAndTime());
		dt.plusMinutes(cacheInterval);
		pageHelper.waitForDateTime(dt);
		
		startStep("Refresh the dashboard and check the score");
		webDriver.closeBrowser();
		webDriver.switchToMainWindow();
		 edoHomePage.clickOnTeachersCorner(true);
		sleep(2);
		String newScoreAvg = getAvgScoresByClassIdAndCourseId(className,
				courseName).get(0)[0];
		 actualUnitScore = dashboardPage
				.getAvgScorePerUnitClassTestScore(0);
		testResultService.assertEquals(newScoreAvg, actualUnitScore,"check refresh after cache refresh");
		System.out.println("Get last update: "+dashboardPage.getDashboardLastUpdateDateAndTime());
		
		
		

	}

}
