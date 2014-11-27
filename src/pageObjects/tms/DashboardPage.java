package pageObjects.tms;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class DashboardPage extends TmsHomePage {

	private static final String SELECT_COURSE = "selectCourse";
	private static final String SELECT_CLASS = "selectClass";

	public DashboardPage(GenericWebDriver webDriver,
			TestResultService testResultService) {
		super(webDriver, testResultService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void selectClassInDashBoard(String className) throws Exception {
		webDriver.selectElementFromComboBox(SELECT_CLASS, className);
	}

	public void selectCourseInDashboard(String courseName) throws Exception {
		webDriver.selectElementFromComboBox(SELECT_COURSE, courseName);
	}

	public void clickOnDashboardGoButton() throws Exception {
		webDriver.waitForElement("goButton", ByTypes.id).click();
	}

	public String getSelectedClass() throws Exception {
		return webDriver.getSelectedValueFromComboBox(SELECT_CLASS);
	}

	public String getSelectedCourse() throws Exception {
		return webDriver.getSelectedValueFromComboBox(SELECT_COURSE);
	}

	public void clickOnReports() throws Exception {
		webDriver.waitForElement("Reports", ByTypes.linkText).click();

	}

	public void clickTmsHome() throws Exception {
		webDriver.waitForElement("homelink", ByTypes.id).click();

	}

}
