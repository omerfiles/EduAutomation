package tests.tms.dashboard;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import Interfaces.TestCaseParams;
import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;

public class DashboardWidgetTests extends DashboardBasicTest {

	@Test
	public void testClassCompletionWidget() throws Exception {

		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		dashboardPage.ClickOnBar();
		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();

		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(1, 2);
		testResultService.assertEquals("Avg. Scores", title, "title not found");
		startStep("Check that chart is displayed");
		dashboardPage.checkIfWidgetHasData(1, 2);
		startStep("Click widget link button and check the report opens");

		startStep("Click on report link, check that report opens and check selected class");
		dashboardPage.clickOnSuccessWidgetButton();

		sleep(4);
		dashboardPage.checkForReportResults();
		testResultService.assertEquals(courseName,
				dashboardPage.getSelectedCourseInReport());
		// testResultService.assertEquals(className,
		// dashboardPage.getSelectedClassInReport());

	}

	@Test
	public void testClassTestScoreWidget() throws Exception {

		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		dashboardPage.ClickOnBar();
		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();

		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(1, 1);
		testResultService.assertEquals("Completion Rate", title,
				"title not found");

		// startStep("Check that chart is displayed");
		// testResultService.assertTrue("Chart has no data",
		// dashboardPage.checkIfWidgetHasData(1, 1));

		startStep("Click widget link button and check the report opens");
		dashboardPage.clickOnCompletionWidgetButton();
		sleep(4);
		dashboardPage.checkForReportResults();
		testResultService.assertEquals(courseName,
				dashboardPage.getSelectedCourseInReport());

	}

	@Test
	@TestCaseParams(testCaseID = { "17097" })
	public void testDataValidationOnClassTestScoreWidget() throws Exception {

		String className = "1 student class";
		String courseName = "2 unis";
		String TeacherUser = "autoTeacher2";
		String[] unitsAvgScores = new String[] { "75", "50" };

		startStep("Login as the teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher(TeacherUser,
				configuration.getProperty("institutaion.subdomain"));
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Select class and course");
		dashboardPage.ClickOnBar();
		dashboardPage.selectClassInDashBoard(className);
		dashboardPage.selectCourseInDashboard(courseName);
		dashboardPage.clickOnDashboardGoButton();

		startStep("Check the avarage score of the units");
		String scoreUnit1 = dashboardPage.getAvgScorePerUnitClassTestScore(0);
		String scoreUnit2 = dashboardPage.getAvgScorePerUnitClassTestScore(1);
		testResultService.assertEquals(unitsAvgScores[0], scoreUnit1);
		testResultService.assertEquals(unitsAvgScores[1], scoreUnit2);

		startStep("Check tooltip");
		dashboardPage.hoverOnClassScoreTooltip(0);
		dashboardPage.checkClassScoreToolipContent("Unit 1", scoreUnit1,null);

	}

	@Test
	public void testTPSWidget() throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		dashboardPage.ClickOnBar();
		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();

		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(1, 2);
		testResultService.assertEquals("Avg. Scores", title, "title not found");
	}

	@Test
	public void testPLTWidget() throws Exception {
		String classNameForTest = "class100";
		String schoolName = "qa";
		autoInstitution.setInstitutionId("5230002");
		autoInstitution.setInstitutionName(schoolName);

		EdoHomePage edoHomePage = pageHelper.loginAsTeacher("autoTeacher",
				"qa.aspx");
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
//		dashboardPage.HoverOnBar();
//		dashboardPage.selectClassInDashBoard(classNameForTest);
//		dashboardPage.hoverOnHeaderAndSelectFromClassCombo(classNameForTest);
		//TODO wait until all dashboard is loaded
		dashboardPage.ClickOnBar();
		dashboardPage.selectClassInDashBoard(classNameForTest);
		dashboardPage.selectCourseInDashboardByIndex(1);
		dashboardPage.ClickOnBar();
		dashboardPage.clickOnDashboardGoButton();

		sleep(10);
		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(3, 1);
		testResultService.assertEquals("Placement Test", title,
				"title not found");

		startStep("Check that chart is displayed");
		dashboardPage.checkIfWidgetHasData(3, 1);
		// hover on first discoveries
		dashboardPage.hoverOnPltWidget();
		System.out.println(dashboardPage.getPltWidgetContent());
		String numOfStudentsPerLevel = dashboardPage
				.getNumberOfStudentsPerPltLevel();
		List<String[]> pltScores = getClassPltScores(classNameForTest,
				schoolName);
		testResultService.assertEquals(pltScores.get(0)[0],
				numOfStudentsPerLevel);

		String numOfStudentWithPLTScores = String
				.valueOf(getNumberOfStudentWithPltScores(pltScores));
		testResultService.assertEquals(numOfStudentWithPLTScores,
				dashboardPage.getPltComletedStudents());

		// TODO - check filled person

		startStep("Click widget link button and check the report opens");

		startStep("Click on report link, check that report opens and check selected class");
		dashboardPage.clickOnPltWidgetButton();

		sleep(4);
		dashboardPage.checkForReportResults();
		// testResultService.assertEquals(className,
		// dashboardPage.getSelectedClassInReport());
	}
}
