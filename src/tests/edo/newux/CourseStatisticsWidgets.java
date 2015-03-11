package tests.edo.newux;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pageObjects.edo.NewUXLoginPage;
import pageObjects.edo.NewUxHomePage;
import Interfaces.TestCaseParams;

public class CourseStatisticsWidgets extends BasicNewUxTest {

	NewUxHomePage homePage;

	@Before
	public void setup() throws Exception {
		super.setup();

		NewUXLoginPage loginPage = new NewUXLoginPage(webDriver,
				testResultService);
		homePage = loginPage.loginAsStudent("we11","12345");
	}
	
	@Test
	@TestCaseParams(testCaseID = { "19856" })
	public void WidgetsElements() throws Exception {
		
		String CompletionLabel = "Course Completion";
		String ScoreLabel = "Average Test Score";
		String TimeLabel = "Time on Task";		
	
	//Verify Course Completion Widget	
	testResultService.assertEquals(true, homePage.isCourseCompletionWidgetExist());
	testResultService.assertEquals("%", homePage.getCourseCompletionWidgetUnitItem());
	testResultService.assertEquals(CompletionLabel, homePage.getCourseCompletionWidgetLabel(CompletionLabel));
	
	//Verify Score Widget Widget	
	testResultService.assertEquals(true, homePage.isScoreWidgetExist());
	testResultService.assertEquals("%", homePage.getScoreWidgetUnitItem());
	testResultService.assertEquals(ScoreLabel, homePage.getScoreWidgetLabel(ScoreLabel));
	
	//Verify Time Widget 
	testResultService.assertEquals(true, homePage.isTimeWidgetExist());
	testResultService.assertEquals(":", homePage.getTimeWidgetUnitsDelimiter());
	testResultService.assertEquals("HR", homePage.getTimeWidgetHoursLabel());
	testResultService.assertEquals("MIN", homePage.getTimeWidgetMinLabel());
	testResultService.assertEquals(TimeLabel, homePage.getTimeWidgetLabel(TimeLabel));
		
	}
	
		
	@After
	public void tearDown() throws Exception {
		homePage.clickOnLogOut();
		super.tearDown();
	}
	
	
	
}
