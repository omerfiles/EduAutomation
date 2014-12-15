package tests.tms.dashboard;

import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import Enums.UserType;
import Interfaces.TestCaseParams;

public class DashboardHeaderTests extends DashboardBasicTest {
	//fixed 11.12.14
	@Test
	@TestCaseParams(testCaseID = { "16908" })
	public void TestKeepSelectedClassAndCourseWhenNavigating() throws Exception {

		String classToSelect = "class015";
		String courseToSelect = "Basic 2 V1";

		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Select class and course");
//		dashboardPage.ClickOnBar();
		sleep(4);
		dashboardPage.hideSelectionBar();
		dashboardPage.selectClassInDashBoard(classToSelect);
		sleep(2);
		dashboardPage.selectCourseInDashboard(courseToSelect);
		dashboardPage.clickOnDashboardGoButton();
		sleep(5);

		startStep("Check number of students");
		testResultService.assertEquals(dbService.getNumberOfStudentsInClass(
				classToSelect, autoInstitution.getInstitutionId()),
				dashboardPage.getNumberOfStudentsPerClass(),
				"Number of students label");

		startStep("Navigate to another report");
		dashboardPage.clickOnReports();

		startStep("Click on the Home button");
		dashboardPage.clickTmsHome();
		sleep(3);
		dashboardPage.hideSelectionBar();
		startStep("Check thet the class and course that where selected, are still appear as selected");
		String currentSelectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classToSelect, currentSelectedClass,
				"Selected class is not: " + classToSelect);
		String currentSelectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseToSelect, currentSelectedCourse,
				"Selected course is not: " + courseToSelect);
	}

	@Test
	@TestCaseParams(testCaseID = { "16991" })
	public void testNavigateToDashboardUsingTheHomeButtonAsTeacher()
			throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Go out of the dashboard");
		sleep(3);
		dashboardPage.clickOnReports();
		sleep(2);

		startStep("Click the Home button");
		dashboardPage.ClickTheHomeButton();
//		dashboardPage
		dashboardPage.hideSelectionBar();
		startStep("Check that the dashboard opens");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertTrue("Classes combo box",
				selectedClass.equals(null) == false);

	}

	@Test
	@TestCaseParams(testCaseID = { "17493" })
	public void testNavigateToDashboardUsingTheHomeButtonAsSchoolAdmin()
			throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsSchoolAdmin();
		sleep(5);
		webDriver.closeAlertByAccept();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Go out of the dashboard");
		sleep(3);
		dashboardPage.clickOnReports();
		sleep(2);

		startStep("Click the Home button");
		dashboardPage.ClickTheHomeButton();
		dashboardPage.hideSelectionBar();
		startStep("Check that the dashboard opens");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertTrue("Classes combo box",
				selectedClass.equals(null) == false);

	}

	// http://vstf2013:8080/tfs/DefaultCollection/EdusoftDev/_workitems#_a=edit&id=17894
	@Test
	@TestCaseParams(testCaseID = { "16997" })
	public void testCheckClassAndCourseWithLastProgressAreSelectedAsTeacher()
			throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();

		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName(),
				UserType.Teahcer);
		sleep(2);
		dashboardPage.hideSelectionBar();
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);
	}
//fails due t bug 17984
	@Test
	@TestCaseParams(testCaseID = { "17347" })
	public void testOpenDashboardAsSupervisorAndSelectTeaccher()
			throws Exception {
		String teacherName = autoInstitution.getTeacherUserName();

		report.startLevel("Login as supervisor");
		EdoHomePage edoHomePage = pageHelper.loginAsSupervisor();
		webDriver.closeAlertByAccept();
		report.startLevel("Click on Teachers corner");
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		report.startLevel("Select a teacher from the combo box");
		getClassAndCourseWithLastProgress(teacherName, UserType.SchoolAdmin);
//		dashboardPage.ClickOnBar();
		dashboardPage.selectTeacherInDashboard(teacherName);
		report.startLevel("Check that class and course with last update are selected");

		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);

	}

	@Test
	@TestCaseParams(testCaseID = { "17492" })
	public void testOpenDashboardAsSchoolAdminAndCheckLastProgressClassAndCourse()
			throws Exception {

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName(),
				UserType.SchoolAdmin);

		report.startLevel("Login as School admin");
		EdoHomePage edoHomePage = pageHelper.loginAsSchoolAdmin();
		report.startLevel("Click on Teachers corner");
		webDriver.closeAlertByAccept();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		dashboardPage.hideSelectionBar();
		report.startLevel("Check selected class and course");
		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);

		String selectedCourse = dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);

	}

	@Test
	@TestCaseParams(testCaseID = { "17005" })
	public void dashboardGridTest() throws Exception {
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);

		startStep("Check dashbaord width");
		int currentWidth = webDriver.getWindowWidth();
		testResultService.assertEquals(1024, currentWidth,
				"Current width is not 1024");

		startStep("Check the width of widgets");
		testResultService.assertEquals(dashboardPage.getWidgetWidth(1, 1),
				getDashboardwidth() / 2, "wrong width of widget");
		
		testResultService.assertEquals(dashboardPage.getWidgetWidth(1, 2),
				getDashboardwidth() / 2, "wrong width of widget");


	}
	
	
	@TestCaseParams(testCaseID = { "17382","17383" })
	public void testHeaderHiddenAndDIsplayed() throws Exception{
		startStep("Login as a teacher and open the dashboard");
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.clickOnTeachersCorner(true);
		
		startStep("Check that header is not displayed by default");
		dashboardPage.checkThatClassComboBoxIsNotDisplayed();
		dashboardPage.hideSelectionBar();
		sleep(2);
		webDriver.printScreen("test");
		dashboardPage.checkThatClassComboBoxIsNotDisplayed();
		
		
	}

}
