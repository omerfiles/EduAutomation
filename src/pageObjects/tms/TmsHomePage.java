package pageObjects.tms;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import Objects.Institution;
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
		webDriver.printScreen("Rating student as teacher", null);
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
		return selectInstitute(instituteName, id, true);
	}

	public TmsHomePage selectInstitute(String instituteName, String id,
			boolean clickGo) throws Exception {
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectSchool", ByTypes.id.toString());
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@id='SelectSchool']//option[@value='" + id + "#"
						+ instituteName + "']", "xpath").click();
		if (clickGo == true) {
			webDriver.waitForElement("//input[@value='  GO  ']", "xpath")
					.click();
		}
		webDriver.switchToParentFrame();
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

		swithchToMainFrame();
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectClass", ByTypes.id.toString());
		Thread.sleep(1000);
		webDriver.waitForElement("//select[@id='SelectClass']//option[2]",
				"xpath").click();
		webDriver.waitForElement("//input[@value='  GO  ']", "xpath").click();
		webDriver.switchToFrame("mainFrame");

		return this;

	}

	public TmsHomePage swithchToMainFrame() throws Exception {
		webDriver.switchToFrame("mainFrame");
		return this;

	}

	public TmsHomePage swithchToFormFrame() throws Exception {
		webDriver.switchToFrame("FormFrame");
		return this;

	}

	public TmsHomePage enterStudentDetails(String studentName) throws Exception {
		webDriver.switchToParentFrame();
		webDriver.switchToFrame("mainFrame");
		webDriver.waitForElement("FirstName", "id").sendKeys(studentName);
		webDriver.waitForElement("LastName", "id").sendKeys(studentName);
		webDriver.waitForElement("UserName", "id").sendKeys(studentName);
		webDriver.waitForElement("//input[@value='Add']", "xpath").click();
		return this;
	}

	public TmsHomePage enterStudentPassword(String userId, String password)
			throws Exception {

		String mainWin = webDriver.switchToFrame("tableFrame");
		webDriver
				.waitForElement("//*[@id='info" + userId + "']/a/img", "xpath")
				.click();
		webDriver.switchToNewWindow();

		Thread.sleep(1000);
		// webDriver.switchToParentFrame();
		String mainPopupWin = webDriver.switchToFrame("FormFrame");
		System.out.println(webDriver.getUrl());
		webDriver.waitForElement("Password", "id").clear();
		webDriver.waitForElement("Password", "id").sendKeys(password);
		webDriver.switchToMainWindow(mainPopupWin);
		webDriver.waitForElement("//input[@value='Submit ']", "xpath").click();
		webDriver.switchToMainWindow(mainWin);

		return this;

	}

	public TmsHomePage clickOnInstitutions() throws Exception {
		webDriver.waitForElementAndClick("Institutions",
				ByTypes.linkText.toString());
		return this;
	}

	public TmsHomePage clickOnAddNewSchool() throws Exception {
		webDriver.waitForElementAndClick("//input[@value='Add New School']",
				"xpath");
		webDriver.switchToNewWindow();
		Thread.sleep(1000);
		return this;

	}

	public TmsHomePage enterNewSchoolDetails(Institution institution)
			throws Exception {
		swithchToFormFrame();
		webDriver.waitForElement("SchoolName", "id").sendKeys(
				institution.getName());
		webDriver.waitForElement("Phone", "name").sendKeys(
				institution.getPhone());
		webDriver.waitForElement("Dname", "id").sendKeys(institution.getHost());
		webDriver.waitForElement("NumOfCustomComp", "id").sendKeys(
				institution.getNumberOfComonents());
		webDriver.waitForElement("NumOfUsers", "id").sendKeys(
				institution.getNumberOfUsers());
		webDriver.waitForElement("NumOfConc", "id").sendKeys(
				institution.getConcurrentUsers());
		webDriver.waitForElement("impType", "id").click();
		String implType = null;
		switch (institution.getSchoolImpType()) {
		case additional:
			implType = "3";
			;
			break;
		case blended:
			implType = "1";
			;
			break;
		case distance:
			implType = "2";
			break;
		}
		webDriver.waitForElement(
				"//select[@id='impType']//option[@value=" + implType + "]",
				"xpath").click();
		webDriver.switchToParentFrame();
		webDriver.waitForElement("//*[@id='Administrator']/a", "xpath").click();
		webDriver.switchToFrame("FormFrame");

		webDriver.waitForElement("FirstName", "name").sendKeys(
				institution.getSchoolAdmin().getName());
		webDriver.waitForElement("LastName", "name").sendKeys(
				institution.getSchoolAdmin().getName());
		webDriver.waitForElement("UserName", "name").sendKeys(
				institution.getSchoolAdmin().getUserName());
		webDriver.waitForElement("Password", "name").sendKeys(
				institution.getSchoolAdmin().getPassword());
		webDriver.waitForElement("Email", "name").sendKeys(
				institution.getSchoolAdmin().getEmail());
		webDriver.switchToParentFrame();
		webDriver.waitForElement("Submitbutton", "name").click();

		return this;

	}

	public TmsHomePage clickOnClasses() throws Exception {
		webDriver.waitForElement("Classes", ByTypes.linkText.toString())
				.click();
		return this;

	}

	public TmsHomePage enterClassName(String classNae) throws Exception {
		webDriver.waitForElement("ClassName", "id").sendKeys(classNae);
		return this;

	}

	public TmsHomePage clickOnAddClass() throws Exception {
		webDriver.waitForElement("AddClass", ByTypes.name.toString()).click();
		return this;
	}

	public TmsHomePage clickOnSettings() throws Exception {
		webDriver.waitForElement("Settings", ByTypes.linkText.toString())
				.click();
		return this;

	}

	public TmsHomePage clickOnFeatures() throws Exception {
		webDriver.waitForElement("Features", ByTypes.linkText.toString())
				.click();
		return this;

	}

	public TmsHomePage selectFeature(String feature) throws Exception {
		webDriver.waitForElement("//select[@id='SelectFeature']",
				ByTypes.xpath.toString()).click();
		webDriver.waitForElement(
				"//select[@id='SelectFeature']//option[@value='" + feature
						+ "']", ByTypes.xpath.toString()).click();
		return this;

	}

	public TmsHomePage clickOnSelfRegistration() throws Exception {

		if (webDriver.waitForElement("SelfRegistrationCheckBox", "id")
				.isSelected() == false) {
			webDriver.waitForElement("SelfRegistrationCheckBox", "id").click();
			webDriver.waitForElement("insertClass", "id").click();
		}

		return this;

	}

	public TmsHomePage selectClassForFelfRegistration(String id)
			throws Exception {
		webDriver.waitForElement("selectClass", "id").click();

		webDriver.waitForElement(
				"//select[@id='selectClass']//option[@value='" + id + "']",
				"xpath").click();
		return this;
	}

	public TmsHomePage clickOnSaveFeature() throws Exception {
		webDriver.waitForElement("//td[@id='SaveTd1']//span", "xpath").click();
		return this;

	}

	public TmsHomePage clickOnInstitutionPackages() throws Exception {
		webDriver.waitForElement("Institution Packages",
				ByTypes.linkText.toString()).click();
		return this;

	}

	public TmsHomePage clickOnAddPackages() throws Exception {
		webDriver.waitForElement("//input[@value='Add Packages']", "xpath")
				.click();
		return this;
		// TODO Auto-generated method stub

	}

	public TmsHomePage selectLevel(String levelName) throws Exception {
		webDriver.waitForElement("selectLevel", ByTypes.name.toString())
				.click();
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@name='selectLevel']//option[text()='" + levelName
						+ "']", "xpath").click();
		webDriver.waitForElement("//button[@name='go']", "xpath").click();
		return this;

	}

	public TmsHomePage selectPackage(String packageID) throws Exception {
		webDriver.waitForElement("//table//tr[" + packageID + "]//td[1]",
				"xpath").click();
		return this;

	}

}
