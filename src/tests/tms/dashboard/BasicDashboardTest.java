package tests.tms.dashboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.tms.DashboardPage;
import tests.misc.EdusoftWebTest;

public class BasicDashboardTest extends EdusoftWebTest {

	protected String classWithLastProgress;
	protected String courseWithLastProgress;

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void getClassAndCourseWithLastProgress(String teacherName)
			throws Exception {
		String[] str = dbService.getClassAndCourseWithLastProgress(teacherName,
				autoInstitution.getInstitutionId());
		classWithLastProgress = str[0];
		courseWithLastProgress = str[1];

	}

	@Test
	public void loginAsTeacher() throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();

		DashboardPage dashboardPage = (DashboardPage) edoHomePage
				.openTeachersCorner(true);

		getClassAndCourseWithLastProgress(autoInstitution.getTeacherUserName());

		String selectedClass = dashboardPage.getSelectedClass();
		testResultService.assertEquals(classWithLastProgress, selectedClass);
		
		String selectedCourse=dashboardPage.getSelectedCourse();
		testResultService.assertEquals(courseWithLastProgress, selectedCourse);
		

	}

}
