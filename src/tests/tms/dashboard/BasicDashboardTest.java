package tests.tms.dashboard;

import java.nio.charset.Charset;
import java.util.List;

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

	public static final String FIRST_DISCOVERIES = "First Discoveries";
	
	private static final int DASHBOARDWIDTH=1024;

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void getClassAndCourseWithLastProgress(String teacherName,
			UserType userType) throws Exception {
		String[] str = dbService.getClassAndCourseWithLastProgress(teacherName,
				autoInstitution.getInstitutionId(), userType);
		classWithLastProgress = str[0];
		courseWithLastProgress = str[1];
	}

	public List<String[]> getClassPltScores(String className,
			String institutionName) throws Exception {
		String sql = textService.getTextFromFile(
				"files/sqlFiles/getPltScore.txt", Charset.defaultCharset());

		sql = sql.replace("%classPara%", className);
		sql = sql.replace("%institutionParam%", institutionName);

		List<List> rsList = dbService.getListFromStoreRrecedure(sql);
		List<String[]> list = rsList.get(2);
		return rsList.get(2);
	}

	public int getNumberOfStudentWithPltScores(List<String[]> list) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			count += Integer.parseInt(list.get(i)[0]);
		}
		System.out.println("with plt:"+count);
		return count;
		
	}

	public static int getDashboardwidth() {
		return DASHBOARDWIDTH;
	}

}
