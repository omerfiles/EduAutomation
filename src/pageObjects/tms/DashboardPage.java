package pageObjects.tms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;
import services.TestResultService;

public class DashboardPage extends TmsHomePage {

	private static final String SELECT_COURSE = "selectCourse";
	private static final String SELECT_CLASS = "selectClass";
	private static final String SELECT_TEACHER = "selectTeacher";

	private static final String GOBUTTONID = "goButton";

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

	public void selectCourseInDashboardByIndex(int index) throws Exception {
		webDriver.selectElementFromComboBoByIndex(SELECT_COURSE, index);
	}

	public void clickOnDashboardGoButton() throws Exception {
		webDriver.waitForElement(GOBUTTONID, ByTypes.id).click();
	}

	public String getSelectedClass() throws Exception {
		return webDriver.getSelectedValueFromComboBox(SELECT_CLASS);
	}

	public String getSelectedCourse() throws Exception {
		String course = webDriver.getSelectedValueFromComboBox(SELECT_COURSE);
		course = course.replaceAll("\\s+$", "");
		return course;
	}

	public void clickOnReports() throws Exception {
		webDriver.waitForElement("Reports", ByTypes.linkText).click();

	}

	public void clickTmsHome() throws Exception {
		webDriver.waitForElement("homelink", ByTypes.id).click();

	}

	public void selectTeacherInDashboard(String teacherName) throws Exception {
		webDriver.selectElementFromComboBox(SELECT_TEACHER, teacherName, true);

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
			webDriver.printScreen("Chart has no data");
			hasData = false;
		}
		return hasData;
	}

	public String getWidgetTitle(int row, int col) throws Exception {
		String title = webDriver.waitForElement(
				"//div[@class='dashboard']//div[" + row + "]//div[" + col
						+ "]//div//div[1]//span", ByTypes.xpath).getText();
		return title;
	}

	public void clickOnSuccessWidgetButton() throws Exception {
		webDriver.waitForElement("successWidgetBtn", ByTypes.id).click();

	}

	public void clickOnCompletionWidgetButton() throws Exception {
		webDriver.waitForElement("completionRateBtn", ByTypes.id).click();

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
						+ unitNumber + "')]", ByTypes.xpath, true, 20)
				.getText();
		return score;

	}

	public void hideSelectionBar() throws Exception {
		// webDriver.hoverOnElement(
		// webDriver.waitForElement("tmsDashNavHandle", ByTypes.className), 50,
		// 50);
		// Thread.sleep(500);
		// if selection bar is shown, wait few seconds to and check if it
		// becomes hidden. if not - click the handle
		if (isSelectionBarShown()) {
			Thread.sleep(3000);
			if (isSelectionBarShown()) {
				// click the handle
				clickDashBoardHandle();
			}
		}

		// webDriver.waitForElement("tmsDashNavHandle", ByTypes.id).click();
		Thread.sleep(1000);
	}

	private void clickDashBoardHandle() throws Exception {
		webDriver.waitForElement("tmsDashNavHandle", ByTypes.id).click();

	}

	private boolean isSelectionBarShown() throws Exception {

		WebElement handleShow = webDriver.waitForElement(
				"//div[@id='tmsDashNavHandle'][contains(@class,'handleOpen')]",
				ByTypes.xpath, false, 5);
		if (handleShow == null) {
			return true;
		} else {
			return false;
		}

	}

	public void hoverOnHeaderAndSelectFromClassCombo(String value)
			throws Exception {
		WebElement hoverElement = webDriver.waitForElement("tmsDefaultBar",
				ByTypes.id);
		// WebElement selectElement= webDriver.waitForElement(SELECT_CLASS,
		// ByTypes.xpath);

		webDriver.HoverOnElementAndmoveToComboBoxElementAndSelectValue(
				hoverElement, SELECT_CLASS, value);
	}

	public String getPltWidgetContent() throws Exception {
		String content = webDriver.getElementHTML(webDriver.waitForElement(
				"PTWidgetCenter", ByTypes.id));
		return content;
	}

	public void hoverOnPltWidget() throws Exception {

		WebElement webElement = webDriver.waitForElement(
				"//div[@id='PTWidget']//canvas[5]", ByTypes.xpath);
		int width = Integer.parseInt(webElement.getAttribute("width"));
		int height = Integer.parseInt(webElement.getAttribute("height"));
		System.out.println("width: " + width + " Height: " + height);

		webDriver.hoverOnElement(webDriver.waitForElement(
				"//div[@id='PTWidget']//canvas[5]", ByTypes.xpath), width / 4,
				height / 4);
		webDriver.printScreen("onhover");

	}

	public String getNumberOfStudentsPerPltLevel() throws Exception {
		String text = webDriver.waitForElement(
				"//div[@id='PTWidgetCenter']//div[@class='coursePercentage']",
				ByTypes.xpath).getText();
		return text;
	}

	public String getPltComletedStudents() throws Exception,
			NumberFormatException {
		String pltStudents = webDriver.waitForElement("PTWidgetTotalNumber",
				ByTypes.id).getText();
		return pltStudents;
	}

	public void hoverOnClassScoreTooltip(int scoreIndex) throws Exception {
		WebElement tooltip = webDriver.waitForElement(
				"//div[contains(@class,'point-" + scoreIndex + "')]",
				ByTypes.xpath);
		webDriver.hoverOnElement(tooltip);

	}

	public void checkClassScoreToolipContent(String unitName, String grade,
			String numOfStudents) throws Exception {
		WebElement tooltip = webDriver.waitForElement("//div[@role='tooltip']",
				ByTypes.xpath);
		System.out.println(webDriver.getElementHTML(tooltip));

	}

	public String getWidgetWidth(int row, int col) throws Exception {
		String actualWidth = webDriver
				.waitForElement(
						"//div[@class='dashboard']//div[" + row + "]//div["
								+ col + "]", ByTypes.xpath)
				.getCssValue("width");

		return actualWidth;

	}

	public void checkThatClassComboBoxIsNotDisplayed() throws Exception {
		webDriver.checkElementNotExist("//select[@id='" + SELECT_CLASS + "']");

	}

	public String[] getClassTestScoreResults() throws Exception {
		WebElement successWidget = webDriver.waitForElement("successWidget",
				ByTypes.id);
		List<WebElement> points = webDriver.getChildElementsByXpath(
				successWidget, "//div[contains(@class,'jqplot-point-label')]");
		String[] scores = new String[points.size()];
		for (int i = 0; i < points.size(); i++) {
			scores[i] = points.get(i).getText();
		}
		return scores;
	}

	public String getDashboardLastUpdateDateAndTime() throws Exception {
		String date = webDriver.waitForElement(
				"//div[@class='LastUpdatedDate']", ByTypes.xpath).getText();
		String time = webDriver.waitForElement(
				"//div[@class='LastUpdatedHrs']", ByTypes.xpath).getText();
		return date + " " + time;
	}

	public boolean getDashboardGoButtonStatus() throws Exception {
		WebElement element = webDriver.waitForElement(GOBUTTONID, ByTypes.id);

		if (element.isEnabled()) {
			return true;
		} else {
			return false;
		}

	}

	public void clickOnTimeOnTaskReport() throws Exception {
		webDriver.waitForElement("TimeOnTask", ByTypes.id).click();

	}

	public String getTpsWidgetTitle() throws Exception {
		return webDriver.waitForElement("unitsBut", ByTypes.id).getText();

	}

	public String getClassLabelText() throws Exception {
		return webDriver.waitForElement("spnClass", ByTypes.id).getText();
	}

	public String getCourseLabelText() throws Exception {
		return webDriver.waitForElement("spnCourse", ByTypes.id).getText();
	}
}
