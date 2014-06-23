package pageObjects;

import java.nio.charset.Charset;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import Enums.ByTypes;
import Objects.Course;
import pageObjects.tms.TmsHomePage;
import services.TextService;
import jsystem.framework.report.Reporter.ReportAttribute;
import junit.framework.Assert;
import drivers.GenericWebDriver;

public class EdoHomePage extends GenericPage {

	private static final String RATING1_TEXT = "Very Good!";

	private static final String RATING2_TEXT = "Good!";

	private static final String RATING3_TEXT = "Keep Working!";

	@Autowired
	TextService textService;

	public String mainWindowName = null;

	public EdoHomePage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	public EdoHomePage checkEraterNotice() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", "id").isDisplayed());
		return this;
	}

	public EdoHomePage clickOnCourses() throws Exception {
		webDriver.waitForElement("My Courses", "linkText").click();
		return this;
	}

	public EdoHomePage clickOnCourseByName(String courseName) throws Exception {
		report.report("Clicking on couse: " + courseName);
		webDriver.waitForElementAndClick(courseName, "linkText");
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
		// stageNumber = stageNumber - 1;
		if (stageNumber > 5) {
			clickOnNextComponent(stageNumber);
		}
		// else{
		// clickOnNextComponent(4);
		// }
		stageNumber = stageNumber - 1;
		webDriver.waitForElement(
				"//ul[@class='ulTasks']//li[@ind='" + stageNumber + "']//a",
				"xpath").click();

		return this;
	}

	public EdoHomePage clickOnNextComponent(int iterations) throws Exception {

		for (int i = 0; i < iterations; i++) {
			// webDriver.waitForElement("//a[@class='tasksBtnext']", "xpath")
			// .click();
			webDriver.waitForElementAndClick("//a[@class='tasksBtnext']",
					"xpath");
			Thread.sleep(500);
		}
		return this;
	}

	public EdoHomePage clickOnLastComponent(int iterations) throws Exception {

		for (int i = 0; i < iterations; i++) {
			webDriver.waitForElement("//a[@class='tasksBtprev']", "xpath")
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

	public EdoHomePage AddWritingAssignmentWithOutSubmit(String text,
			TextService textService) throws Exception {
		// String assayText = textService.getTextFromFile(assayTextFileName,
		// Charset.defaultCharset());
		String assayText = text;
		textService.setClipboardText(assayText);
		// webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']",
		// assayText,
		// "elm1_ifr");
		webDriver.waitForElement("elm1_ifr", ByTypes.id.toString());
		String mainWindow = webDriver.switchToFrame("elm1_ifr");
		WebElement textArea = webDriver.waitForElement("//body[@id='tinymce']",
				"xpath");
		webDriver.pasteTextFromClipboard(textArea);
		webDriver.switchToMainWindow(mainWindow);
		return this;
	}

	public EdoHomePage submitWritingAssignment(String assayTextFileName,
			TextService textService) throws Exception {
		String assayText = textService.getTextFromFile(assayTextFileName,
				Charset.defaultCharset());

		System.out.println("Clipboard text is set to: " + assayText);
		textService.setClipboardText(assayText);
		// webDriver.swithcToFrameAndSendKeys("//body[@id='tinymce']",
		// assayText,
		// "elm1_ifr");
		Thread.sleep(3000);
		String mainWindow = webDriver.switchToFrame("elm1_ifr");
		WebElement textArea = webDriver.waitForElement("//body[@id='tinymce']",
				"xpath");
		webDriver.pasteTextFromClipboard(textArea);
		webDriver.switchToMainWindow(mainWindow);
		Thread.sleep(3000);
		// webDriver.waitForElement("//a[@title='Submit']", "xpath").click();
		// webDriver.closeAlertByAccept();
		// webDriver.closeAlertByAccept();
		clickOnSubmitAssignment();
		return this;
	}

	public EdoHomePage clickOnSubmitAssignment() throws Exception {
		webDriver.waitForElement("//a[@title='Submit']", "xpath").click();
		Thread.sleep(3000);
		webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		return this;
	}

	public String getAssignmentTextFromEditor() throws Exception {
		String mainWindow = webDriver.switchToFrame("elm1_ifr");
		String text = webDriver
				.waitForElement("//body[@id='tinymce']", "xpath").getText();
		webDriver.switchToMainWindow(mainWindow);
		return text;
	}

	public EdoHomePage checkEraterNoticeAppear() throws Exception {
		Assert.assertTrue(webDriver.waitForElement("ERaterNotice", "id")
				.isDisplayed());
		return this;
	}

	public EdoHomePage clickOnMyAssignments() throws Exception {
		webDriver.waitForElementAndClick("myAssignments", "id");
		return this;
	}

	public EdoHomePage switchToAssignmentsFrame() throws Exception {
		mainWindowName = webDriver.switchToFrame(webDriver.waitForElement(
				"cboxIframe", ByTypes.className.toString()));
		return this;

	}

	public EdoHomePage clickOnWritingAssignmentsTab(String courseName)
			throws Exception {
		webDriver.waitForElement("spWriting", "id").click();
		WebElement td = webDriver.getTableTdByName("//*[@id='myWritingTbl']",
				courseName);
		td.click();
		// webDriver.waitForElement(courseName,
		// ByTypes.partialLinkText.toString()).click();
		// webDriver.waitForElement("View", "linkText").click();
		return this;
	}

	public EdoHomePage clickToViewAssignment(String courseName)
			throws Exception {
		webDriver
				.waitForElement(courseName, ByTypes.partialLinkText.toString())
				.click();
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
					// webDriver.checkElementNotExist(
					// "//h1[@id='log']//div[@class='noEntry']",
					// "User is already logged in");

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

	public TmsHomePage openTeachersCorner() throws Exception {
		webDriver.waitForElement("Teacher's Corner", "linkText").click();
		webDriver.sleep(2000);
		webDriver.switchToNewWindow();
		System.out.println("URL:" + webDriver.getUrl());
		webDriver.switchToFrame(0);
		// webDriver.waitForElement("a5", "id").click();
		// webDriver.waitForElement("//a[@href='../Report/writingAssignments.aspx']",
		// ByTypes.xpath.toString()).click();
		return new TmsHomePage(webDriver);
	}

	public EdoHomePage addWritingAssignment(Course course, String textFile)
			throws Exception {
		clickOnCourses();
		// String courseName = "Basic 3 2012";
		clickOnCourseByName(course.getName());
		waitForCourseDetailsToBeDisplayed(course.getName());
		clickOnCourseUnit(course.getCourseUnits().get(0).getName());
		clickOntUnitComponent(course.getCourseUnits().get(0).getUnitComponent()
				.get(0).getName(), "Practice");
		ClickOnComponentsStage(Integer.valueOf((course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getStageNumber())));
		Thread.sleep(10000);
		submitWritingAssignment(textFile, textService);
		return this;
	}

	public EdoHomePage addWritingAssignmentWithoutSubmitting(Course course,
			String text, TextService textService) throws Exception {
		clickOnCourses();
		// String courseName = "Basic 3 2012";
		clickOnCourseByName(course.getName());
		waitForCourseDetailsToBeDisplayed(course.getName());
		clickOnCourseUnit(course.getCourseUnits().get(0).getName());
		clickOntUnitComponent(course.getCourseUnits().get(0).getUnitComponent()
				.get(0).getName(), "Practice");
		Thread.sleep(5000);
		ClickOnComponentsStage(Integer.valueOf((course.getCourseUnits().get(0)
				.getUnitComponent().get(0).getStageNumber())));

		AddWritingAssignmentWithOutSubmit(text, textService);

		return this;
	}

	public EdoHomePage clickToSaveAssignment() throws Exception {
		WebElement saveBtn = webDriver.waitForElement("elm1_save_",
				ByTypes.id.toString());
		saveBtn.click();
		String alertText = webDriver.getAlertText(10);
		Assert.assertEquals("Your draft has been saved.", alertText);
		webDriver.closeAlertByAccept();
		webDriver
				.waitForElement(
						"//a[@id='elm1_save_']//span[@class='mceIcon mce_save_ disable']",
						ByTypes.xpath.toString());
		return this;
	}

	@Override
	public GenericPage OpenPage(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public EdoHomePage clickOnSeeFeedback() throws Exception {
		webDriver.waitForElement("See Feedback", ByTypes.linkText.toString())
				.click();
		;
		return this;
	}

	public EdoHomePage clickOnSeeFeedbackAndTryAgsin() throws Exception {
		webDriver.waitForElement("See feedback and try again",
				ByTypes.linkText.toString()).click();
		;
		return this;
	}

	public EdoHomePage clickOnFeedbackMoreDetails() throws Exception {
		webDriver.waitForElement("//div[@id='okButton']//a",
				ByTypes.xpath.toString()).click();
		;
		return this;

	}

	public EdoHomePage editFeedbackAssignmentText(String newText)
			throws Exception {
		webDriver.switchToMainWindow(mainWindowName);
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath.toString()));
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath.toString()));
		webDriver.swithcToFrameAndSendKeys("//*[@id='tinymce']", newText,
				false, "elm1_ifr");
		return this;

	}

	public EdoHomePage clickOnFeedbackSubmitBtn() throws Exception {
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath.toString()));
		webDriver.switchToFrame(webDriver.waitForElement(
				"//div[@id='cboxLoadedContent']//iframe",
				ByTypes.xpath.toString()));

		webDriver.waitForElement("//div[@id='btSubmit']/a",
				ByTypes.xpath.toString()).click();
		Thread.sleep(3000);
		webDriver.closeAlertByAccept();
		Thread.sleep(3000);
		// webDriver.switchToAlert();
		// webDriver.sendKey(Keys.ENTER);
		webDriver.closeAlertByAccept();
		Thread.sleep(5000);
		return this;
	}

	public EdoHomePage checkRatingFromTeacher(int rating) throws Exception {
		// String mainWin =
		// webDriver.switchToFrame(webDriver.waitForElement("//iframe[class='cboxIframe']",
		// "xpath"));
		// webDriver.switchToFrame(webDriver.waitForElement("//iframe[class='cboxIframe']",
		// "xpath"));
		// webDriver.switchToFrame(webDriver.waitForElement(
		// "cboxIframe", ByTypes.className.toString()));

		String grade = webDriver.waitForElement("//div[@class='choosenGrade']",
				"xpath").getText();

		switch (rating) {
		case 1:
			Assert.assertEquals(grade, RATING1_TEXT);
		case 2:
			Assert.assertEquals(grade, RATING2_TEXT);
			break;
		case 3:
			Assert.assertEquals(grade, RATING3_TEXT);
			break;

		}
		// webDriver.switchToMainWindow(mainWin);
		return this;

	}

	public EdoHomePage clickOnPlayButton() throws Exception {
		webDriver.waitForElementAndClick("CTrackerPlayBtn", "id");
		return this;

	}

	public EdoHomePage clickOnSeeExplanation() throws Exception {
		// webDriver.waitForElementAndClick("seeExplanation", "id");
		webDriver.waitForElement("seeExplanation", "id").click();
		return this;

	}

	public EdoHomePage checkExplanationPopupOpens() throws Exception {

		String mainWin = webDriver.switchToNewWindow();

		// webDriver.waitForElement("See Explanation",
		// ByTypes.partialLinkText.toString()).isDisplayed();
		webDriver.waitForElement("PopupTitle", ByTypes.className.toString());
		org.junit.Assert.assertEquals("See Explanation", webDriver
				.waitForElement("PopupTitle", ByTypes.className.toString())
				.getText());
		webDriver.waitForElement("Close Window", "linkText").click();
		;
		webDriver.switchToMainWindow(mainWin);
		return this;
	}

	public EdoHomePage clickOnSeeText() throws Exception {
		webDriver.waitForElement("SeeText", "id").click();
		;
		return this;
	}

	public EdoHomePage checkSeeTextPopupOpens() throws Exception {
		String mainWin = webDriver.switchToPopup();
		// webDriver.waitForElement("PopupTitle",ByTypes.className.toString()).getText();
		org.junit.Assert.assertEquals(
				"See Text",
				webDriver.waitForElement("PopupTitle",
						ByTypes.className.toString()).getText());
		webDriver.waitForElement("Close Window", "linkText").click();
		;
		webDriver.switchToMainWindow(mainWin);

		return this;

	}

	public EdoHomePage clickOnPracticeTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=2]", "xpath")
				.click();
		return this;

	}
	public EdoHomePage dragAnswerToElement(String dataId,WebElement toElement) throws Exception {
		WebElement answer = webDriver.waitForElement("//div[@data-id='"
				+ dataId + "']", "xpath");
//		WebElement questionFiled = webDriver.waitForElement(
//				"//span[@data-id='1_1']", "xpath");
		webDriver.dragAndDropElement(answer, toElement);
		return this;

	}

	public EdoHomePage dragAnswer(String dataId) throws Exception {
		WebElement answer = webDriver.waitForElement("//div[@data-id='"
				+ dataId + "']", "xpath");
		WebElement questionFiled = webDriver.waitForElement(
				"//span[@data-id='1_1']", "xpath");
		webDriver.dragAndDropElement(answer, questionFiled);
		return this;

	}

	public EdoHomePage clickOnClearAnswer() throws Exception {
		webDriver.waitForElement("Restart", "id").click();
		;
		return this;

	}

	public EdoHomePage checkIfAnswerIsCleared(String answerId) throws Exception {
		webDriver.checkElementNotExist("//span[@data-id='1_1'][@ans='"
				+ answerId + "']");
		return this;

	}

	public EdoHomePage clickOnTestTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=3]", "xpath")
				.click();
		;
		return this;

	}

	public EdoHomePage clickOnStartTest() throws Exception {
		webDriver.waitForElement("//div[@id='testIntro']//a", "xpath").click();
		;
		return this;

	}

	public EdoHomePage clickOnSubmitTest() throws Exception {
		webDriver.waitForElement("Submit", ByTypes.linkText.toString()).click();
		;
		return this;
	}

	public EdoHomePage approveTest() throws Exception {

		String mainWin = webDriver.switchToNewWindow(1);
		webDriver.waitForElement("btnOk", "id").click();
		;
		webDriver.switchToMainWindow();
		return this;
	}

	public EdoHomePage checkStudentScore(String score) throws Exception {
		String actualScore = webDriver.waitForElement(
				"//span[@class='TestCongratulation']", "xpath").getText();
		Assert.assertEquals("Score is incorrect or not found", score,
				actualScore);
		return this;
	}

	public EdoHomePage clickOnExploreTab() throws Exception {
		webDriver.waitForElement("//ul[@class='ulSteps']//li[@id=1]", "xpath")
				.click();
		return this;

	}

	public EdoHomePage checkUnitHeaderText(String expectedHeaderText)
			throws Exception {
		String text = webDriver.waitForElement(
				"//div[@class='mainAreaDivShort']//h1", "xpath").getText();
		Assert.assertEquals("Text not found", text, expectedHeaderText);
		return this;

	}

	public void markVocabRadioBtnAnswer(String questionId, String answer)
			throws Exception {
		webDriver
				.waitForElement(
						"//div[contains(@id,'q"
								+ questionId
								+ "')][@name='txt_div']//div//div//div//span[contains(text(),'"
								+ answer + "')]", "xpath").click();

	}

	public void checkVacabRadioBtnAnswer(String questionId, boolean trueAnswer,
			String answer) throws Exception {
		String check;
		if (trueAnswer == true) {
			check = "vCheck";
		} else {
			check = "xCheck";
		}
		// webDriver.waitForElement("//div[contains(@id,'q"+questionId+"')][contains(@class,'"+check+"']",
		// "xpath");
		webDriver.waitForElement("//div[contains(@id,'q" + questionId
				+ "')][@name='txt_div'][contains(@class,'" + check
				+ "')]//div//div//div//span[contains(text(),'" + answer + "')]",
				"xpath");
	}

	public EdoHomePage clickOnCheckAnswers() throws Exception {
		webDriver.waitForElement("CheckAnswer", "id").click();
		return this;

	}
}
