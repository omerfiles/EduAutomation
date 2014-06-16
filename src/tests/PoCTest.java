package tests;

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

import Objects.Student;
import Objects.Teacher;
import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;

public class PoCTest extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}
	
	@Test
	public void IEPrintScreen()throws Exception{
		
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		webDriver.printScreen();
		

	}
	@Test
	public void TestSafari()throws Exception{
		
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		webDriver.printScreen();
	}
	@Test
	public void testDbNullQuery()throws Exception{
		String sql="select * from writing where UserId='52313630000004' and EssayText like '%You are do%'";
		dbService.getStringFromQuery(sql);
	}
	
	@Test
	public void OpenTmsCloseItAndOpenEdoAndLogin()throws Exception{
		
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());

		Student student = new Student();
		student.setUserName(config.getStudentUserName());
		student.setPassword(config.getStudentPassword());
		student.setId(dbService.getUserIdByUserName(student.getUserName()));
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		
		webDriver.deleteCookiesAndRefresh();
		Teacher teacher = new Teacher(config.getProperty("teacher.username"),
				config.getProperty("teacher.password"));
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoHomePage = edoLoginPage.login(teacher);
		edoHomePage.waitForPageToLoad();
		TmsHomePage tmsHomePage = edoHomePage.openTeachersCorner();
		
		
		
		
		webDriver.quitBrowser();
		webDriver.init();
		webDriver.openUrl(getSutAndSubDomain());
		edoLoginPage.login(student);
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
	System.out.println(dbService.getUserIdByUserName(config.getStudentUserName()));	
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
//		EdoHomePage edoHomePage = new EdoHomePage(webDriver);
		// edoHomePage.logOut();
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
		student.setUserName(config.getProperty("student.user.name"));
		student.setPassword(config.getProperty("student.user.password"));
		String userId = dbService.getUserIdByUserName(student.getUserName());
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

	

	

	public void testOpenURL(GenericWebDriver webDriver) throws Exception {
		webDriver.init();
		webDriver.openUrl("http://tms.engdis.com/");
	}
}
