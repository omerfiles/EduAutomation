package tests.tms.dashboard;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;

public class DashboardWidgetTests extends BasicDashboardTest {

	@Test
	public void testClassCompletionWidget() throws Exception {

		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

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
				.openTeachersCorner(true);

		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();

		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(1, 1);
		testResultService.assertEquals("Completaion Rate", title,
				"title not found");

		startStep("Check that chart is displayed");
		testResultService.assertTrue("Chart has no data",
				dashboardPage.checkIfWidgetHasData(1, 1));

		startStep("Click widget link button and check the report opens");
		dashboardPage.clickOnCompletionWidgetButton();
		sleep(4);
		dashboardPage.checkForReportResults();
		testResultService.assertEquals(courseName,
				dashboardPage.getSelectedCourseInReport());
	}
	
	@Test
	public void testCompletionRateWidget()throws Exception{
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();
		
		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(2, 1);
		testResultService.assertEquals("Completaion Rate", title,
				"title not found");
	}
	
	@Test
	public void testTPSWidget()throws Exception{
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();
		
		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(2, 1);
		testResultService.assertEquals("Completaion Rate", title,
				"title not found");
	}
	
	@Test
	public void testPLTWidget()throws Exception{
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		String className = dashboardPage.getSelectedClass();
		String courseName = dashboardPage.getSelectedCourse();
		
		startStep("Check widget title");
		String title = dashboardPage.getWidgetTitle(3, 1);
		testResultService.assertEquals("Placement Test", title,
				"title not found");
		
		startStep("Check that chart is displayed");
		dashboardPage.checkIfWidgetHasData(3, 1);
		startStep("Click widget link button and check the report opens");

		startStep("Click on report link, check that report opens and check selected class");
		dashboardPage.clickOnPltWidgetButton();

		sleep(4);
		dashboardPage.checkForReportResults();
//		testResultService.assertEquals(className,
//				dashboardPage.getSelectedClassInReport());
	}

}
