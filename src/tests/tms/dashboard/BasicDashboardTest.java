package tests.tms.dashboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import Enums.UserType;
import Interfaces.TestCaseParams;
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

	public void getClassAndCourseWithLastProgress(String teacherName,UserType userType)
			throws Exception {
		String[] str = dbService.getClassAndCourseWithLastProgress(teacherName,
				autoInstitution.getInstitutionId(),userType);
		classWithLastProgress = str[0];
		courseWithLastProgress = str[1];
	}
	
	
	

	

	
}
