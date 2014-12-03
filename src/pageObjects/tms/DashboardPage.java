package pageObjects.tms;

import org.openqa.selenium.WebElement;

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

	public void selectTeacherInDashboard(String teacherName) {
		// TODO Auto-generated method stub

	}

	public WebElement getWidgetElement(int row, int col, String title)
			throws Exception {
		WebElement element = webDriver
				.waitForElement("//div[@class='dashboard']//div[" + row
						+ "]//div[" + col + "]", ByTypes.xpath);
		return element;
	}

	public boolean checkIfWidgetHasData(int row, int col) throws Exception {
		boolean hasData = true;
		WebElement element = webDriver.waitForElement(
				"//div[@class='dashboard']//div[" + row + "]//div[" + col
						+ "]//[div[contains(@class,'nodata')]'", ByTypes.xpath,
				false, webDriver.getTimeout());
		if (element == null) {
			hasData = false;
		}
		return hasData;
	}

	public String getWidgetTitle(int row, int col) throws Exception {
		String title = webDriver.waitForElement(
				"//div[@class='dashboard']//div[" + row + "]//div[" + col
						+ "]//div//div//span", ByTypes.xpath).getText();
		return title;
	}

	public void clickOnSuccessWidgetButton() throws Exception {
		webDriver.waitForElement("successWidgetBtn", ByTypes.id).click();

	}

	public void clickOnCompletionWidgetButton() throws Exception {
		webDriver.waitForElement("completaionRateBtn", ByTypes.id).click();

	}

	public void clickOnPltWidgetButton() throws Exception {
		webDriver.waitForElement("PTWidgetBtn", ByTypes.id).click();

	}

	public String getNumberOfStudentsPerClass() throws Exception {
		String number = webDriver.waitForElement(
				"//div[@class='studentsCounterNumber']", ByTypes.xpath)
				.getText();
		return number;
	}

	public String getAvgScorePerUnitClassTestScore(int unitNumber)
			throws Exception {
		String score = webDriver.waitForElement(
				"//div[@id='successWidget']//div[contains(@class,'point-"
						+ unitNumber + "')]", ByTypes.xpath).getText();
		return score;

	}

	public void HoverOnBar() throws Exception {
		webDriver.hoverOnElement(webDriver.waitForElement("tmsDefaultBar",
				ByTypes.id));
	}
}
