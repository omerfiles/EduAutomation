package tests.tms.dashboard;

import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import Interfaces.TestCaseParams;

public class DashboardHeaderTests extends BasicDashboardTest{

	
	@Test
	@TestCaseParams(testCaseID = { "16908" })
	public void TestKeepSelectedClassAndCourseWhenNavigating() throws Exception {

		String classToSelect = "class015";
		String courseToSelect = "Basic 2";

		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		startStep("Select class and course");
		dashboardPage.selectClassInDashBoard(classToSelect);
		sleep(2);
		dashboardPage.selectCourseInDashboard(courseToSelect);
		dashboardPage.clickOnDashboardGoButton();
		sleep(5);

		startStep("Navigate to another report");
		dashboardPage.clickOnReports();

		startStep("Click on the Home button");
		dashboardPage.clickTmsHome();
		sleep(3);
		startStep("Check thet the class and course that where selected, are still appear as selected");
		String currentSelectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classToSelect, currentSelectedClass,
				"Selected class is not: " + classToSelect);
		String currentSelectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseToSelect, currentSelectedCourse,
				"Selected course is not: " + courseToSelect);
	}
	
	@Test
	@TestCaseParams(testCaseID = { "16997" })
	public void testCheckClassAndCourseWithLastProgressAreSelected() throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();

		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName());

		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);
	}
	

}
