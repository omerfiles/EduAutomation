package pageObjects.tms;

import org.openqa.selenium.WebElement;

import Enums.ByTypes;
import drivers.GenericWebDriver;
import pageObjects.GenericPage;

public class TmsHomePage extends GenericPage {

	public TmsHomePage(GenericWebDriver webDriver) {
		super(webDriver);
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
		System.out.println(webDriver.getUrl());
		Thread.sleep(2000);
		webDriver.switchToFrame(1);
		Thread.sleep(2000);
		WebElement td = webDriver.getTableTdByName("//*[@id='tblTestsReportGrid']",
				courseName);

		return this;
	}

}
