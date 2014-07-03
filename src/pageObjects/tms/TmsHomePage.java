package pageObjects.tms;

import java.util.Calendar;

import org.junit.Assert;
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
		webDriver.waitForElement("homelink", ByTypes.id);
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TmsHomePage cliclOnReports() throws Exception {
		webDriver.waitForElement("Reports", ByTypes.linkText).click();
		return this;
	}

	public TmsHomePage clickOnWritingAssignments() throws Exception {
		webDriver.waitForElement(
				"//a[@href='../Report/writingAssignments.aspx']",
				ByTypes.xpath).click();
		return this;
	}

	public TmsHomePage clickOnAssignment(String assignmentPartialText)
			throws Exception {
		webDriver.waitForElement(assignmentPartialText, ByTypes.partialLinkText)
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
		// int elapsedTime = 0;
		// int maxTime = 120;
		// WebElement td = null;
		// while (elapsedTime < maxTime) {
		// td = webDriver.getTableTdByName("//*[@id='tblTestsReportGrid']",
		// studentName);
		// if (td != null) {
		// break;
		// } else {
		// logger.info("Waiting for student assignment for another 5 seconds");
		// elapsedTime = elapsedTime + 5;
		// Thread.sleep(5000);
		// }
		// }
		//
		// // td.click();
		// webDriver.clickOnElement(td);

		webDriver.waitForElement(
				"//td//div[contains(text(),'" + courseName + "')]", ByTypes.xpath)
				.click();
		;
		webDriver.switchToMainWindow(mainFrame);
		return this;
	}

	public TmsHomePage clickOnAssignmentSummary() throws Exception {

		webDriver.switchToFrame("mainFrame");
		String mainFrame = webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath));
		webDriver.waitForElement("//div[@class='right closeBt']", ByTypes.xpath)
				.click();
		webDriver.waitForElement("Summary", ByTypes.linkText).click();
		// webDriver.switchToMainWindow(mainFrame);
		return this;
	}

	public TmsHomePage clickOnApproveAssignmentButton() throws Exception {
		webDriver.waitForElement("Approve", ByTypes.partialLinkText)
				.click();
		return this;
	}

	public TmsHomePage reteAssignment(int rating) throws Exception {
		webDriver.waitForElement("//tr//td//input[@id='" + rating + "']",
				ByTypes.xpath).click();
		webDriver.printScreen("Rating student as teacher", null);
		return this;
	}

	public TmsHomePage sendFeedback() throws Exception {
		webDriver.waitForElement("btSubmit", ByTypes.id).click();
		return this;
	}

	public TmsHomePage clickOnRateAssignmentButton() throws Exception {
		webDriver.waitForElement("Rate", ByTypes.linkText).click();
		return this;
	}

	public TmsHomePage clickOnTeachers() throws Exception {
		webDriver.waitForElement("Teachers", ByTypes.linkText)
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
		webDriver.waitForElementAndClick("SelectSchool", ByTypes.id);
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@id='SelectSchool']//option[@value='" + id + "#"
						+ instituteName + "']", ByTypes.xpath).click();
		if (clickGo == true) {
			webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath)
					.click();
		}
		webDriver.switchToParentFrame();
		return this;
	}

	public TeacherDetailsPage clickOnAddNewTeacher() throws Exception {

		webDriver.waitForElementAndClick("//input[@value='Add New Teacher']",
				ByTypes.xpath);
		webDriver.switchToNewWindow();
		return new TeacherDetailsPage(webDriver);

	}

	public TmsHomePage clickOnStudents() throws Exception {
		webDriver.waitForElement("Students", ByTypes.linkText)
				.click();
		return this;

	}

	public TmsHomePage selectClass(String className) throws Exception {

		swithchToMainFrame();
		String mainWin = webDriver.switchToFrame("FormFrame");
		webDriver.waitForElementAndClick("SelectClass", ByTypes.id);
		Thread.sleep(1000);
		webDriver.waitForElement("//select[@id='SelectClass']//option[2]",
				ByTypes.xpath).click();
		webDriver.waitForElement("//input[@value='  GO  ']", ByTypes.xpath).click();
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
		webDriver.waitForElement("FirstName", ByTypes.id).sendKeys(studentName);
		webDriver.waitForElement("LastName", ByTypes.id).sendKeys(studentName);
		webDriver.waitForElement("UserName", ByTypes.id).sendKeys(studentName);
		webDriver.waitForElement("//input[@value='Add']", ByTypes.xpath).click();
		return this;
	}

	public TmsHomePage enterStudentPassword(String userId, String password)
			throws Exception {

		String mainWin = webDriver.switchToFrame("tableFrame");
		webDriver
				.waitForElement("//*[@id='info" + userId + "']/a/img", ByTypes.xpath)
				.click();
		webDriver.switchToNewWindow();

		Thread.sleep(1000);
		// webDriver.switchToParentFrame();
		String mainPopupWin = webDriver.switchToFrame("FormFrame");
		System.out.println(webDriver.getUrl());
		webDriver.waitForElement("Password", ByTypes.id).clear();
		webDriver.waitForElement("Password", ByTypes.id).sendKeys(password);
		webDriver.switchToMainWindow(mainPopupWin);
		webDriver.waitForElement("//input[@value='Submit ']", ByTypes.xpath).click();
		webDriver.switchToMainWindow(mainWin);

		return this;

	}

	public TmsHomePage clickOnInstitutions() throws Exception {
		webDriver.waitForElementAndClick("Institutions",
				ByTypes.linkText);
		return this;
	}

	public TmsHomePage clickOnAddNewSchool() throws Exception {
		webDriver.waitForElementAndClick("//input[@value='Add New School']",
				ByTypes.xpath);
		webDriver.switchToNewWindow();
		Thread.sleep(1000);
		return this;

	}

	public TmsHomePage enterNewSchoolDetails(Institution institution)
			throws Exception {
		swithchToFormFrame();
		webDriver.waitForElement("SchoolName", ByTypes.id).sendKeys(
				institution.getName());
		webDriver.waitForElement("Phone", ByTypes.name).sendKeys(
				institution.getPhone());
		webDriver.waitForElement("Dname", ByTypes.id).sendKeys(institution.getHost());
		webDriver.waitForElement("NumOfCustomComp", ByTypes.id).sendKeys(
				institution.getNumberOfComonents());
		webDriver.waitForElement("NumOfUsers", ByTypes.id).sendKeys(
				institution.getNumberOfUsers());
		webDriver.waitForElement("NumOfConc", ByTypes.id).sendKeys(
				institution.getConcurrentUsers());
		webDriver.waitForElement("impType", ByTypes.id).click();
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
				ByTypes.xpath).click();
		webDriver.switchToParentFrame();
		webDriver.waitForElement("//*[@id='Administrator']/a", ByTypes.xpath).click();
		webDriver.switchToFrame("FormFrame");

		webDriver.waitForElement("FirstName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getName());
		webDriver.waitForElement("LastName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getName());
		webDriver.waitForElement("UserName", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getUserName());
		webDriver.waitForElement("Password", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getPassword());
		webDriver.waitForElement("Email", ByTypes.name).sendKeys(
				institution.getSchoolAdmin().getEmail());
		webDriver.switchToParentFrame();
		webDriver.waitForElement("Submitbutton", ByTypes.name).click();

		return this;

	}

	public TmsHomePage clickOnClasses() throws Exception {
		webDriver.waitForElement("Classes", ByTypes.linkText)
				.click();
		return this;

	}

	public TmsHomePage enterClassName(String classNae) throws Exception {
		webDriver.waitForElement("ClassName", ByTypes.id).sendKeys(classNae);
		return this;

	}

	public TmsHomePage clickOnAddClass() throws Exception {
		webDriver.waitForElement("AddClass", ByTypes.name).click();
		return this;
	}

	public TmsHomePage clickOnSettings() throws Exception {
		webDriver.waitForElement("Settings", ByTypes.linkText)
				.click();
		return this;

	}

	public TmsHomePage clickOnFeatures() throws Exception {
		webDriver.waitForElement("Features", ByTypes.linkText)
				.click();
		return this;

	}

	public TmsHomePage selectFeature(String feature) throws Exception {
		webDriver.waitForElement("//select[@id='SelectFeature']",
				ByTypes.xpath).click();
		webDriver.waitForElement(
				"//select[@id='SelectFeature']//option[@value='" + feature
						+ "']", ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage clickOnSelfRegistration() throws Exception {

		if (webDriver.waitForElement("SelfRegistrationCheckBox", ByTypes.id)
				.isSelected() == false) {
			webDriver.waitForElement("SelfRegistrationCheckBox", ByTypes.id).click();
			webDriver.waitForElement("insertClass", ByTypes.id).click();
		}

		return this;

	}

	public TmsHomePage selectClassForFelfRegistration(String id)
			throws Exception {
		webDriver.waitForElement("selectClass", ByTypes.id).click();

		webDriver.waitForElement(
				"//select[@id='selectClass']//option[@value='" + id + "']",
				ByTypes.xpath).click();
		return this;
	}

	public TmsHomePage clickOnSaveFeature() throws Exception {
		webDriver.waitForElement("//td[@id='SaveTd1']//span", ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage clickOnInstitutionPackages() throws Exception {
		webDriver.waitForElement("Institution Packages",
				ByTypes.linkText).click();
		return this;

	}

	public TmsHomePage clickOnAddPackages() throws Exception {
		webDriver.waitForElement("//input[@value='Add Packages']", ByTypes.xpath)
				.click();
		return this;
		// TODO Auto-generated method stub

	}

	public TmsHomePage selectLevel(String levelName) throws Exception {
		webDriver.waitForElement("selectLevel", ByTypes.name)
				.click();
		Thread.sleep(1000);
		webDriver.waitForElement(
				"//select[@name='selectLevel']//option[text()='" + levelName
						+ "']", ByTypes.xpath).click();
		webDriver.waitForElement("//button[@name='go']", ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage selectPackage(String packageID) throws Exception {
		webDriver.waitForElement("//table//tr[" + packageID + "]//td[1]",
				ByTypes.xpath).click();
		return this;

	}

	public TmsHomePage selectPackageStartDate(String packageId)
			throws Exception {
		webDriver.waitForElement("//a[@name='anchor" + packageId + "']//img",
				ByTypes.xpath).click();
		int currentDay = dbService.getCurrentDay();
		String nainWin = webDriver.switchToPopup();
		webDriver.waitForElement("//td//a[text()='" + currentDay + "']",
				ByTypes.xpath).click();
		webDriver.switchToMainWindow(nainWin);
		swithchToFormFrame();
		return this;
	}

	public TmsHomePage enterPackageQuantity(String packageId, String amount)
			throws Exception {
		webDriver.waitForElement("number" + packageId, ByTypes.name)
				.sendKeys(amount);
		return this;

	}

	public TmsHomePage clickOnSubmitButton() throws Exception {
		webDriver.waitForElement("Submitbutton", ByTypes.name)
				.click();
		return this;

	}

	public TmsHomePage checkPackageExist(String packageName) throws Exception {
		webDriver.waitForElement(
				"//tbody//tr//td[text()='" + packageName + "']", ByTypes.xpath)
				.isDisplayed();
		return this;

	}

	public TmsHomePage checkClassNameIsDisplayed(String className)
			throws Exception {
		webDriver.waitForElement(
				"//tbody//tr//td//a[text()='" + className + "']", ByTypes.xpath)
				.isDisplayed();
		return this;

	}

	public void checkRemoveCommentButtonStatus(boolean status,
			boolean switchToFrames) throws Exception {

		if (switchToFrames) {
			swithchToMainFrame();
			webDriver.switchToFrame(webDriver.waitForElement(
					"//iframe[@class='cboxIframe']", ByTypes.xpath));
		}
		if (status == false) {
			webDriver.waitForElement(
					"//a[@id='butRemove'][@class='button remove disabled']",
					ByTypes.xpath);
		} else {
			webDriver.waitForElement(
					"//a[@id='butRemove'][@class='button remove']", ByTypes.xpath);
		}

	}

	public void selectFeedbackComment(String commentId) throws Exception {
		webDriver.waitForElement(commentId, ByTypes.id).click();

	}

	public void clickTheRemoveCommentButton() throws Exception {
		webDriver.waitForElement("butRemove", ByTypes.id).click();
		;

	}

	public void checkFeedbackCommentUnderline(String commentId,
			boolean underline) throws Exception {

		if (underline) {
			webDriver.waitForElement("//id[@" + commentId
					+ "][contains(@class,'underline')]", ByTypes.xpath);
		} else {
			webDriver.checkElementNotExist("//id[@" + commentId
					+ "][contains(@class,'underline')]");
		}

	}

	public void checkfeedbackCommentText(boolean underLineds) {
		// TODO Auto-generated method stub

	}

	public void clickOnXFeedback() throws Exception {
		webDriver.switchToFrame("mainFrame");
		String mainFrame = webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath));
		webDriver.waitForElement("//div[@class='right closeBt']", ByTypes.xpath)
				.click();
		webDriver.switchToMainWindow(mainFrame);

	}

}
