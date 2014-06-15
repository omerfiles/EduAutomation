package pageObjects.tms;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.GenericPage;

public class TmsHomePage extends GenericPage {

	public TmsHomePage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		webDriver.switchToFrame("mainFrame");
		webDriver.waitForElement("homelink", "id");
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TmsHomePage cliclOnReports() throws Exception {
		webDriver.waitForElement("Reports", "linkText").click();
		return this;
	}

	public TmsHomePage clickOnWritingAssignments() throws Exception {
		webDriver.waitForElement(
				"//a[@href='../Report/writingAssignments.aspx']",
				ByTypes.xpath.toString()).click();
		return this;
	}

	public TmsHomePage clickOnAssignment(String assignmentPartialText)
			throws Exception {
		webDriver.waitForElement(assignmentPartialText, "partialLinkText")
				.click();
		return this;
	}

	public TmsHomePage clickOnStudentAssignment(String studentName,
			String courseName) throws Exception {
		// int index=webDriver.getTableRowIndexByParam("tblTestsReportGrid",
		// "4", courseName,ByTypes.partialLinkText.toString());
		// webDriver.waitForElement("//table[@id='tblTestsReportGrid']//tr["+index+"]//td[3]//div",
		// ByTypes.xpath.toString()).click();
		Thread.sleep(2000);
		String mainFrame = webDriver.switchToFrame("ReviewRequiredReport");
		Thread.sleep(2000);
		int elapsedTime = 0;
		int maxTime = 120;
		WebElement td = null;
		while (elapsedTime < maxTime) {
			td = webDriver.getTableTdByName("//*[@id='tblTestsReportGrid']",
					studentName);
			if (td != null) {
				break;
			} else {
				logger.info("Waiting for student assignment for another 5 seconds");
				elapsedTime = elapsedTime + 5;
				Thread.sleep(5000);
			}
		}

		// td.click();
		webDriver.clickOnElement(td);
		webDriver.switchToMainWindow(mainFrame);
		return this;
	}

	public TmsHomePage clickOnAssignmentSummary() throws Exception {

		webDriver.switchToFrame("mainFrame");
		String mainFrame = webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath.toString()));
		webDriver.waitForElement("//div[@class='right closeBt']", "xpath")
				.click();
		webDriver.waitForElement("Summary", "linkText").click();
		// webDriver.switchToMainWindow(mainFrame);
		return this;
	}

	public TmsHomePage clickOnApproveAssignmentButton() throws Exception {
		webDriver.waitForElement("Approve", ByTypes.partialLinkText.toString())
				.click();
		return this;
	}

	public TmsHomePage reteAssignment(int rating) throws Exception {
		webDriver.waitForElement("//tr//td//input[@id='" + rating + "']",
				"xpath").click();
		return this;
	}

	public TmsHomePage sendFeedback() throws Exception {
		webDriver.waitForElement("btSubmit", "id").click();
		return this;
	}

	public TmsHomePage clickOnRateAssignmentButton() throws Exception {
		webDriver.waitForElement("Rate", ByTypes.linkText.toString()).click();
		return this;
	}

	public TmsHomePage clickOnTeachers() throws Exception {
		webDriver.waitForElement("Teachers", ByTypes.linkText.toString())
				.click();
		return this;

	}

	public TmsHomePage selectInstitute(String instituteName, String id)
			throws Exception {
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectSchool", ByTypes.id.toString());
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@id='SelectSchool']//option[@value='" + id + "#"
						+ instituteName + "']", "xpath").click();
		webDriver.waitForElement("//input[@value='  GO  ']", "xpath").click();
		webDriver.switchToFrame("mainFrame");
		return this;
	}

	public TeacherDetailsPage clickOnAddNewTeacher() throws Exception {
		webDriver.waitForElementAndClick("//input[@value='Add New Teacher']",
				"xpath");
		webDriver.switchToNewWindow();
		return new TeacherDetailsPage(webDriver);

	}

	public TmsHomePage clickOnStudents() throws Exception {
		webDriver.waitForElement("Students", ByTypes.linkText.toString())
				.click();
		return this;

	}

	public TmsHomePage selectClass(String className) throws Exception {

		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectClass", ByTypes.id.toString());
		Thread.sleep(1000);
		webDriver.waitForElement("//select[@id='SelectClass']//option[2]",
				"xpath").click();
		;
		webDriver.waitForElement("//input[@value='  GO  ']", "xpath").click();
		webDriver.switchToFrame("mainFrame");

		return this;

	}

	public TmsHomePage swithchToMainFrame() throws Exception {
		webDriver.switchToFrame("mainFrame");
		return this;

	}

	public TmsHomePage enterStudentDetails(String studentName) throws Exception {
		webDriver.waitForElement("FirstName", "id").sendKeys(studentName);
		webDriver.waitForElement("LastName", "id").sendKeys(studentName);
		webDriver.waitForElement("UserName", "id").sendKeys(studentName);
		webDriver.waitForElement("//input[@value='Add']", "xpath").click();
		return this;
	}

	public TmsHomePage enterStudentPassword(String userId, String password)
			throws Exception {
		String mainWin = webDriver.switchToFrame("tableFrame");
		webDriver.waitForElement("//*[@id='info" + userId + "']/a/img", "xpath")
				.click();
		webDriver.switchToNewWindow();
		String mainPopupWin= webDriver.switchToFrame("FormFrame");
		webDriver.waitForElement("Password", "id").clear();
		webDriver.waitForElement("Password", "id").sendKeys(password);
		webDriver.switchToMainWindow(mainPopupWin);
		webDriver.waitForElement("//input[@value='Submit ']", "xpath").click();
		
		return this;

	}

}
