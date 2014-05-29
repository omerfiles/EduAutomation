package pageObjects;

import java.nio.charset.Charset;

import org.openqa.selenium.WebElement;

import services.TextService;
import junit.framework.Assert;
import drivers.GenericWebDriver;

public class EdoHomePage extends GenericPage {

	public EdoHomePage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	public EdoHomePage checkEraterNotice() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", "id")
				.isDisplayed());
		return this;
	}

	public EdoHomePage clickOnCourses() throws Exception {
		webDriver.waitForElement("My Courses", "linkText").click();
		return this;
	}

	public EdoHomePage clickOnCourseByName(String courseName) throws Exception {
		webDriver.waitForElement(courseName, "linkText").click();
		return this;
	}

	public EdoHomePage waitForCourseDetailsToBeDisplayed(String courseName)
			throws Exception {
		Assert.assertEquals(
				courseName,
				webDriver.waitForElement("//*[@id='mainAreaTD']/div/h1",
						"xpath").getText());
		return this;
	}

	public EdoHomePage waitForUnitDetailsToBeDisplayed(String unitName)
			throws Exception {
		Assert.assertEquals(
				unitName,
				webDriver.waitForElement("//*[@id='mainAreaTD']/div/h1",
						"xpath").getText());
		return this;
	}

	public EdoHomePage logOut() throws Exception {
		webDriver.waitForElement("Log Out", "linkText").click();
		webDriver.switchToPopup();
		// webDriver.closeAlertByAccept();
		webDriver.waitForElement("btnOk", "id").click();
		return this;
	}

	public EdoHomePage tempLogout() throws Exception {
		webDriver.waitForElement("//*[@id='navTable']/tbody/tr/td[4]/a",
				"xpath").click();
		Thread.sleep(3000);
		webDriver.switchToNewWindow();
		// webDriver.closeAlertByAccept();
		webDriver.waitForElement("btnOk", "id").click();
		return this;
	}

	public EdoHomePage clickOnCourseUnit(String unitName) throws Exception {
		webDriver.waitForElement(unitName, "linkText").click();
		return this;

	}

	public EdoHomePage clickOntUnitComponent(String componentName,
			String componentType) throws Exception {
		webDriver.waitForElement(componentName, "linkText").click();
		webDriver.waitForElement(componentType, "linkText").click();
		return this;
	}

	public EdoHomePage ClickOnComponentsStage(int stageNumber) throws Exception {
//		stageNumber = stageNumber - 1;
		if (stageNumber > 5) {
			clickOnNextComponent(stageNumber);
		}
//		else{
//			clickOnNextComponent(4);
//		}
		stageNumber = stageNumber - 1;
		webDriver.waitForElement("//li[@ind='" + stageNumber + "']", "xpath")
				.click();
		return this;
	}

	public EdoHomePage clickOnNextComponent(int iterations) throws Exception {

		for (int i = 0; i < iterations; i++) {
			webDriver.waitForElement("//a[@class='tasksBtnext']", "xpath")
					.click();
			Thread.sleep(500);
		}
		return this;
	}

	public EdoHomePage registerStudent(String firstName, String LastName,
			String UserName, String Password, int classNumber) throws Exception {
		String mainFrame = webDriver.switchToFrame(1);

		webDriver.waitForElement("fe1", "id").sendKeys(firstName);
		webDriver.waitForElement("fe2", "id").sendKeys(LastName);
		webDriver.waitForElement("fe3", "id").sendKeys(UserName);
		webDriver.waitForElement("fe4", "id").sendKeys(Password);
		webDriver.waitForElement(
				"//input[@type='radio'][@value='_" + classNumber + "']",
				"xpath").click();
		webDriver.waitForElement("/html/body/div[3]/div[2]/div/div[3]/a",
				"xpath").click();

		String alertText = webDriver.getAlertText(60);
		Assert.assertTrue(alertText
				.contains("The user has been successfully registered."));
		return this;
	}

	public EdoHomePage submitWritingAssignment(String assayTextFileName,TextService textService)
			throws Exception {
		String assayText = textService.getTextFromFile(assayTextFileName,
				Charset.defaultCharset());
		textService.setClipboardText(assayText);
//		webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']", assayText,
//				"elm1_ifr");
	String mainWindow=	webDriver.switchToFrame("elm1_ifr");
		WebElement textArea=	webDriver.waitForElement("//body[@id='tinymce']", "xpath");
		webDriver.pasteTextFromClipboard(textArea);
		webDriver.switchToMainWindow(mainWindow);
		Thread.sleep(3000);
		webDriver.waitForElement("//a[@title='Submit']", "xpath").click();
		webDriver.closeAlertByAccept();
		webDriver.closeAlertByAccept();
		return this;
	}

	public EdoHomePage checkEraterNoticeAppear() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", "id")
				.isDisplayed());
		return this;
	}

	public EdoHomePage clickOnMyAssignments() throws Exception {
		webDriver.waitForElement("myAssignments", "id").click();
		return this;
	}
	public EdoHomePage switchToAssignmentsFrame()throws Exception{
		webDriver.switchToFrame(webDriver.waitForElement("cboxIframe", "class"));
		return this;
	}
	public EdoHomePage clickOnWritingAssignmentsTab()throws Exception{
		webDriver.waitForElement("spWriting", "id").click();
		return this;
	}
	public EdoHomePage clickToViewAssignment(String courseName)throws Exception{
		webDriver.waitForElement(courseName, "linkText").click();
		webDriver.waitForElement("View", "linkText").click();
		return this;
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {

		int waitTimeOut = 60;
		int elapsedTime = 0;
		while (elapsedTime < waitTimeOut) {
			try {
				Boolean pageLoaded = webDriver.waitForElement("navTable", "id",
						10, false).isDisplayed();
				if (pageLoaded == true) {
					break;
				}
				if (webDriver.waitForElement("cont", "id").isDisplayed()) {
					System.out.println("progress bar is displayed");
					Thread.sleep(5000);
					elapsedTime = elapsedTime + 5;
					System.out.println("Slepping for 5 seconds");
					continue;
				}
			} catch (Exception e) {
				try {

					logger.info("Checking if user is logged in already");
					// Assert.assertFalse(
					// "User is logged in. Test will now fail",
					// webDriver.waitForElement(
					// "//h1[@id='log']//div[@class='noEntry']",
					// "xpath").isDisplayed());
					webDriver.checkElementNotExist(
							"//h1[@id='log']//div[@class='noEntry']",
							"User is already logged in");

					System.out.println("trying to close popup");
					webDriver.closeAlertByAccept();
					break;
				} catch (Exception j) {

				}
				continue;
			}

		}
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
