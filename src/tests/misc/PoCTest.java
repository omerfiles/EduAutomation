package tests.misc;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jsystem.extensions.report.junit.JUnitReporter;
import jsystem.extensions.report.xml.XmlReporter;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.monte.audiodemo.AudioRecorder;

import Enums.ByTypes;
import Interfaces.TestCaseParams;
import Objects.Student;
import Objects.Teacher;
import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import services.MyTestRunner;
import ddf.minim.*;

//@RunWith(MyTestRunner.class)
public class PoCTest extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void testFirefox() throws Exception {

		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		webDriver.printScreen();

	}

	@Test
	public void TestSafari() throws Exception {

		System.out.println(institutionService.getInstitution()
				.getInstitutionId());
		;
	}

	@Test
	public void TestUserLoggedIn() throws Exception {
		AudioRecorder audioRecorder = new AudioRecorder(new File(
				"files\\sound111.avi"));
		audioRecorder.start();

		sleep(20);
		audioRecorder.stop();

	}

	@Test
	public void testDbNullQuery1() throws Exception {
		dbService
				.getStringFromQuery("select classId from class where Name='class1'");
	}

	@Test
	public void testDbNullQuery2() throws Exception {

	}

	@Test
	public void testNoParam() throws Exception {

	}

	@Test
	@TestCaseParams(testCaseID = "14521", ignoreBrowsers = { "" }, isRun = false)
	public void OpenTmsCloseItAndOpenEdoAndLogin() throws Exception {

		System.out.println("Test run");
	}

	@Test
	public void testAssignment() throws Exception {
		report.startLevel("Login to Edo", EnumReportLevel.CurrentPlace);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		// edoLoginPage.openPage();
		edoLoginPage.typeUserNameAndPass("isr4", "12345");
		EdoHomePage edoHomePage = edoLoginPage.submitLogin();
		report.stopLevel();

		report.startLevel("Open home page and start a writing drill",
				EnumReportLevel.CurrentPlace);
		edoHomePage.waitForPageToLoad();
		edoHomePage.clickOnCourses();
		String courseName = "Advanced 2";
		edoHomePage.clickOnCourseByName(courseName);
		edoHomePage.waitForCourseDetailsToBeDisplayed(courseName);
		edoHomePage.clickOnCourseUnit("Interviews");
		edoHomePage.clickOntUnitComponent("Dear Dotty", "Practice");
		edoHomePage.ClickOnComponentsStage(6);
		edoHomePage.submitWritingAssignment("files/textFiles/assay.txt",
				textService);

		edoHomePage.checkEraterNoticeAppear();
		edoHomePage.clickOnMyAssignments();
		edoHomePage.switchToAssignmentsFrame();
		// edoHomePage.clickOnWritingAssignmentsTab();

		report.stopLevel();

	}

	@Test
	public void getUserId() throws Exception {
		System.out.println(dbService.getUserIdByUserName(
				configuration.getStudentUserName(),
				autoInstitution.getInstitutionId()));
	}

	@Test
	public void AEONTest() throws Exception {
		String[] users = textService.getStrArrayFromCsv(
				"files/textFiles/teachers.csv", 0);
		String[] passwords = textService.getStrArrayFromCsv(
				"files/textFiles/teachers.csv", 1);

		for (int i = 0; i < users.length; i++) {
			if (users[i] != null) {
				System.out.println("user name is:" + users[i]);
				testAEON(users[i], passwords[i], i + 557);
			}

		}

	}

	@Test
	public void testLoginLogout() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		// edoLoginPage
		// .openEdoLoginPage("http://edo.engdis.com/aeonreading/Runtime/LoginNew.aspx");
		edoLoginPage.typeUserNameAndPass("teach55", "teach55");
		EdoHomePage edoHomePage = edoLoginPage.submitLogin();
		edoHomePage.waitForPageToLoad();

		// edoHomePage.tempLogout();
	}

	@Test
	public void readFromVsv() throws Exception {
		String[] users = textService.getStrArrayFromCsv(
				"files/textFiles/teachers.csv", 0);
		String[] passwords = textService.getStrArrayFromCsv(
				"files/textFiles/teachers.csv", 1);

		for (int i = 0; i < 5; i++) {
			// System.out.println(users[i]+"   "+passwords[i]);

		}

	}

	@After
	public void tearDown() throws Exception {
		// EdoHomePage edoHomePage = new EdoHomePage(webDriver);
		// edoHomePage.logOut();
		System.out.println("Test tear down");
		super.tearDown();

	}

	public void testAEON(String user, String password, int index)
			throws Exception {

		report.startLevel("Creating student for teacher:" + user);
		webDriver.init("http://localhost:4444", null);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		// edoLoginPage
		// .openEdoLoginPage("http://edo.engdis.com/aeonreading/Runtime/LoginNew.aspx");
		edoLoginPage.typeUserNameAndPass(user, password);
		EdoHomePage edoHomePage = edoLoginPage.submitLogin();
		edoHomePage.waitForPageToLoad();

		int classNumber;
		if (index % 2 == 0) {
			classNumber = 1;
		} else {
			classNumber = 2;
		}
		report.report("Student user name is: TestAuto" + index);
		edoHomePage.registerStudent("studentTestAuto" + index,
				"studentTestAuto" + index, "TestAuto" + index, "TestAuto"
						+ index, classNumber);
		webDriver.quitBrowser();
		report.stopLevel();
	}

	@Test
	public void testDB() throws Exception {
		Student student = new Student();
		student.setUserName(configuration.getProperty("student.user.name"));
		student.setPassword(configuration.getProperty("student.user.password"));
		String userId = dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId());
		report.report(userId);
	}

	@Test
	public void testGetNodesList() throws Exception {
		// String sqlxml = dbService
		// .getStringFromQuery("select EraterXML from Erater where writingId=1");
		String jsonStr = dbService
				.getStringFromQuery("select EraterJson from Erater where writingId=1");
		// List<String[]>arrList=
		// netService.getListFromXmlNode(netService.getXmlFromString(sqlxml),
		// "/WAT:DetailInfo");
		List<String[]> jsonList = netService.getListFromJson(jsonStr,
				"sections", "details", new String[] { "feedback", "length",
						"offset" });
		eraterService.printArrayList(jsonList);
		System.out.println("**********  after the sort ***********");
		eraterService.sortArrayList(jsonList);
		eraterService.printArrayList(jsonList);

	}

	@Test
	@TestProperties(name = "testConvertEraterXml")
	public void testConvertEraterXml() throws Exception {

		List<String[]> writingIds = textService
				.getStr2dimArrFromCsv("files/csvFiles/writingIds.csv");
		boolean pass = true;
		for (int i = 0; i < writingIds.size(); i++) {

			if (eraterService
					.compareJsonAndXmlByWritingId(writingIds.get(i)[0]) == false) {
				pass = false;
			}
		}
		Assert.assertTrue("Some unmatches found", pass);
		//

	}

	@Test
	public void testReadXml() throws Exception {
		String sqlxml = dbService
				.getStringFromQuery("select EraterXML from Erater where writingId=131");
		report.report("RAW XML: " + sqlxml);
		List<String[]> xmlList = netService.getListFromXmlNode(
				netService.getXmlFromString(sqlxml), "/WAT:DetailInfo");
		xmlList = eraterService
				.removeHiddenCodesAndConvertToFeedbackCodes(xmlList);

	}

	@Test
	public void readFromCsv() throws Exception {
		List<String[]> list = textService
				.getStr2dimArrFromCsv("files/textFiles/teachers.csv");

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i)[0] + " " + list.get(i)[1]);
		}
	}

	@Test
	public void testDeleteUpdate() throws Exception {
		pageHelper.loginAsStudent();

	}

	@Test
	public void testPrintScreen() throws Exception {
		pageHelper.loginAsStudent();
//		webDriver.printScreen("test print message 1", null);
//		webDriver.printScreen("test print message 2");
		webDriver.waitForElement("b", ByTypes.id);
	}

	@Test
	public void testRemoveComment() throws Exception {

		Student student = new Student();
		student.setUserName(configuration.getStudentUserName());
		student.setPassword(configuration.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId()));

		EdoHomePage edoHomePage = pageHelper.loginAsTeacher();
		edoHomePage.waitForPageToLoad();
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		// String newWritingId = eraterService.getWritingIdByUserIdAndTextStart(
		// student.getId(), newText);
		// eraterService.checkWritingIsProcessed(newWritingId);
		tmsHomePage.clickOnWritingAssignments();
		String courseName = pageHelper.getCourses().get(3).getName();
		tmsHomePage.clickOnStudentAssignment(student.getUserName(), courseName);

		startStep("Check that the Remove button is disabled when no comment is selected");
		tmsHomePage.clickOnXFeedback();
		tmsHomePage.checkRemoveCommentButtonStatus(false, true);

		startStep("Select a comment and check the remve button");
		tmsHomePage.selectFeedbackComment("0_0");
		tmsHomePage.checkRemoveCommentButtonStatus(true, false);

		startStep("click the remove button and check the texts");
		tmsHomePage.clickTheRemoveCommentButton();
		tmsHomePage.checkIfCommentedTextIsInderlined("0_0", false);
		// tmsHomePage.checkfeedbackCommentText(false);
	}

	@Test
	public void checkCoveredFeedback() throws Exception {
		List<String[]> feedbacks = textService
				.getStr2dimArrFromCsv("files/csvFiles/UncoverFeedback.csv");
		for (int i = 0; i < feedbacks.size(); i++) {
			String feedbackId = feedbacks.get(i)[0];
			// System.out.println(feedbackId);
			String sql = "select count(*) from Erater where EraterJson like '%feedback\":\""
					+ feedbackId + "\"%'";

			String result = dbService.getStringFromQuery(sql);
			report.report("Count for feedback: " + feedbackId + " is: "

			+ result);

		}

	}

	@Test
	public void testSendHttp() throws Exception {
		pageHelper.loginAsStudent();
		webDriver.waitForElement("aaa", ByTypes.id);
	}

	public void testOpenURL(GenericWebDriver webDriver) throws Exception {
		webDriver.init();
		webDriver.openUrl("http://tms.engdis.com/");
	}
}
