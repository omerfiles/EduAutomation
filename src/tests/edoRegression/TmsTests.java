package tests.edoRegression;

import javax.print.attribute.standard.PageRanges;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import drivers.GenericWebDriver;
import drivers.IEWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TeacherDetailsPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import tests.misc.EdusoftWebTest;
import Enums.ByTypes;
import Enums.SchoolImpTypes;
import Objects.Institution;
import Objects.SchoolAdmin;
import Objects.Student;
import Objects.Teacher;
import Objects.UserObject;

public class TmsTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	public void testLoginToEdoAsTeacher() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setUserName(autoInstitution.getTeacherUserName());
		teacher.setPassword(autoInstitution.getTeacherPassword());

		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.login(teacher);
	}

	@Test
	public void RegisterTeacherAndLogin() throws Exception {
		report.startLevel("Open TMS and login as TMS Admin",
				EnumReportLevel.CurrentPlace);
		this.testCaseId = "5782";
		String teacherName = "teacher" + dbService.sig(6);
		String teacherPassword = "12345";

		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(configuration.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		report.startLevel("Go to teachers section and select the institute",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnTeachers();
		String institutionId = configuration.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId);
		report.stopLevel();

		report.startLevel("Enter new teacher details",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.swithchToMainFrame();
		TeacherDetailsPage teacherDetailsPage = tmsHomePage
				.clickOnAddNewTeacher();

		teacherDetailsPage.typeTeacherFirstName(teacherName);
		teacherDetailsPage.typeTeacherLastName(teacherName);
		teacherDetailsPage.typeTeacherUserName(teacherName);
		teacherDetailsPage.typeTeacherPassword(teacherPassword);
		teacherDetailsPage.addClass();
		teacherDetailsPage.clickOnSubmit();

		report.stopLevel();

		report.startLevel("Logout from TMS and login as the new teacher",
				EnumReportLevel.CurrentPlace);
		webDriver.quitBrowser();
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Teacher teacher = new Teacher(teacherName, teacherPassword);
		EdoHomePage homePage = edoLoginPage.login(teacher);
		homePage.waitForPageToLoad();
		report.stopLevel();

	}

	// @Test
	// public void RegisterStudentAndLogin() throws Exception {
	// RegisterStudentAndLogin(chromeWebDriver);
	// }

	// public void RegisterStudentAndLoginIE() throws Exception {
	// RegisterStudentAndLogin(ieWebDriver);
	// }
	// @Test
	// public void RegisterStudentAndLoginFirefox() throws Exception {
	// RegisterStudentAndLogin(firefoxDriver);
	// }
	@Test
	public void RegisterStudentAndLogin() throws Exception {
		report.startLevel("Open TMS and login as TMS Admin",
				EnumReportLevel.CurrentPlace);
		this.testCaseId = "5783";
		String studentName = "student" + dbService.sig(6);
		String studentPassword = "12345";
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(configuration.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		report.startLevel("Go to students section and select the institute",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnStudents();
		String institutionId = configuration.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId, false);
		Thread.sleep(1000);
		tmsHomePage.selectClass(configuration.getProperty("classname"));

		report.stopLevel();

		report.startLevel("Enter new student details",
				EnumReportLevel.CurrentPlace);

		// if(webDriver instanceof IEWebDriver){
		// Thread.sleep(1000);
		// }

		tmsHomePage.enterStudentDetails(studentName);
		String userId = dbService.getUserIdByUserName(studentName,
				autoInstitution.getInstitutionId());
		tmsHomePage.enterStudentPassword(userId, studentPassword);

		report.stopLevel();
		report.startLevel("Login as student", EnumReportLevel.CurrentPlace);
		webDriver.closeBrowser();
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Student student = new Student();
		student.setUserName(studentName);
		student.setPassword(studentPassword);
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		edoHomePage.waitForPageToLoad();

		report.stopLevel();

	}

	@Test
	public void createNewInstitution() throws Exception {
		report.startLevel("Open TMS and login as TMS Admin",
				EnumReportLevel.CurrentPlace);
		this.testCaseId = "5779";
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(configuration.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		report.startLevel("Click on Institutions and click on Add new school",
				EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnInstitutions();
		tmsHomePage.clickOnAddNewSchool();

		report.stopLevel();
		report.startLevel("Enter new institution details",
				EnumReportLevel.CurrentPlace);
		Institution institution = new Institution();
		institution.setName("autoSchool" + dbService.sig(5));
		report.report("School name is: " + institution.getName());
		institution.setPhone("985644456");
		institution.setConcurrentUsers("100");
		institution.setNumberOfComonents("20");
		institution.setNumberOfUsers("100");
		institution.setSchoolImpType(SchoolImpTypes.blended);
		institution.setHost(configuration.getProperty("sut.url").replace(
				"http://", "")
				+ institution.getName());
		SchoolAdmin schoolAdmin = new SchoolAdmin();
		String adminUserName = "admin" + dbService.sig(6);
		schoolAdmin.setUserName(adminUserName);
		schoolAdmin.setName(adminUserName);
		schoolAdmin.setPassword("12345");
		schoolAdmin.setEmail(adminUserName + "@edusoft.co.il");
		institution.setSchoolAdmin(schoolAdmin);
		institution.setEmail(adminUserName + "@edusoft.co.il");
		tmsHomePage.enterNewSchoolDetails(institution);

		report.stopLevel();

		report.startLevel("Verify in the DB that institution is created",
				EnumReportLevel.CurrentPlace);
		dbService.verifyInstitutionCreated(institution);
		report.stopLevel();
	}
	//Test case 7348
	public void testSelfRegistration() throws Exception {

		startStep("Open TMS and create new class");

		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();
		tmsHomePage.clickOnClasses();
		String institutionId = configuration.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId);
		String className = "class" + dbService.sig(3);
		report.report("Class name is: " + className);
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.enterClassName(className);
		tmsHomePage.clickOnAddClass();
		startStep("Set class for self registration");
		tmsHomePage.clickOnSettings();
		tmsHomePage.clickOnFeatures();
		String feature = "SR";
		tmsHomePage.swithchToFormFrame();
		tmsHomePage.selectFeature(feature);
		tmsHomePage.selectInstitute(instituteName, institutionId);
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.clickOnSelfRegistration();
		String classId = dbService.getClassIdByName(className,
				autoInstitution.getInstitutionId());
		tmsHomePage.selectClassForFelfRegistration(classId);
		tmsHomePage.clickOnSaveFeature();

		startStep("Open institution URL and self register as a student");
		webDriver.closeBrowser();
		webDriver.init();
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.clickOnSelfRegistraton();
		webDriver.switchToNewWindow(1);
		String studentName = "student" + dbService.sig(6);
		webDriver.switchToFrame("content");
		edoLoginPage.enterStudentRegUserName(studentName);
		edoLoginPage.enterStudentRegFirstName(studentName);
		edoLoginPage.enterStudentRegLastName(studentName);
		edoLoginPage.enterStudentRegPassword("12345");
		edoLoginPage.enterStudentEmail(studentName + "@edusoft.co.il");
		edoLoginPage.clickOnRegister();

		startStep("Check that user is added to the class");

	}

	// Test case 7663
	@Test
	public void testAssigningValidPackagesToInstitution() throws Exception {
		startStep("Init test data");
		this.testCaseId = "7663";
		String packageId = "231";
		String institutionId = autoInstitution.getInstitutionId();
		String instituteName = dbService.getInstituteNameById(institutionId);
		String lavalName = "F-A3";

		startStep("Login to TMS as Admin");
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();
		startStep("Create new instutution");
		Institution institution = new Institution();
		institution.setName("autoSchool" + dbService.sig(5));
		report.report("School name is: " + institution.getName());
		institution.setPhone("985644456");
		institution.setConcurrentUsers("100");
		institution.setNumberOfComonents("20");
		institution.setNumberOfUsers("100");
		institution.setSchoolImpType(SchoolImpTypes.blended);
		institution.setHost(configuration.getProperty("sut.url").replace(
				"http://", "")
				+ institution.getName());
		SchoolAdmin schoolAdmin = new SchoolAdmin();
		String adminUserName = "admin" + dbService.sig(6);
		schoolAdmin.setUserName(adminUserName);
		schoolAdmin.setName(adminUserName);
		schoolAdmin.setPassword("12345");
		schoolAdmin.setEmail(adminUserName + "@edusoft.co.il");
		institution.setSchoolAdmin(schoolAdmin);
		institution.setEmail(adminUserName + "@edusoft.co.il");
		tmsHomePage.clickOnInstitutions();
		tmsHomePage.clickOnAddNewSchool();
		tmsHomePage.enterNewSchoolDetails(institution);
		sleep(10);

		startStep("Assign package to the institution");

		webDriver.switchToMainWindow();
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.clickOnInstitutionPackages();
		tmsHomePage.selectInstitute(institution.getName(),
				dbService.getInstituteIdByName(institution.getName()));
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.clickOnAddPackages();
		webDriver.switchToNewWindow(1);

		tmsHomePage.selectLevel(lavalName);
		tmsHomePage.swithchToFormFrame();

		tmsHomePage.selectPackage("1");
		tmsHomePage
				.selectPackageStartDate(packageId, dbService.getCurrentDay());
		tmsHomePage.enterPackageQuantity(packageId, "100");
		webDriver.switchToTopMostFrame();
		tmsHomePage.clickOnSubmitButton();

		startStep("Check that package was added");
		webDriver.switchToMainWindow();
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.checkPackageExist("F-A3/12M_18M");

	}

	// Test case id 7667
	// Assign Packages to A Class
	@Test
	public void testAssignPackagesToClass() throws Exception {
		startStep("Init Test data");
		String packageId = "6543";
		startStep("Login to TMS as Admin");
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();

		startStep("Create new class");
		tmsHomePage.clickOnClasses();
		String institutionId = autoInstitution.getInstitutionId();
		String instituteName = autoInstitution.getInstitutionName();
		tmsHomePage.selectInstitute(instituteName, institutionId);
		String className = "class" + dbService.sig(3);
		report.report("Class name is: " + className);
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.enterClassName(className);
		tmsHomePage.clickOnAddClass();

		startStep("Go To Curriculum and click on Assign Package");
		tmsHomePage.clickOnCurriculum();

		tmsHomePage.clickOnAssignPackages();
		tmsHomePage.selectInstitute(instituteName, institutionId);
		tmsHomePage.swithchToMainFrame();
		// tmsHomePage.swithchToFormFrame();

		tmsHomePage.selectPackage("4");
		sleep(2);
		String classId = dbService.getClassIdByName(className,
				autoInstitution.getInstitutionId());
		tmsHomePage.markClassForPackageAssignment(classId, packageId);
		tmsHomePage.clickOnSaveFeature();
		
		

	}

	// Test case id 7256

	@Test
	public void testCreateNewClass() throws Exception {
		startStep("Open TMS and create new class");
		this.testCaseId = "7256";
		TmsHomePage tmsHomePage = pageHelper.loginToTmsAsAdmin();
		tmsHomePage.clickOnClasses();
		String institutionId = autoInstitution.getInstitutionId();
		String instituteName = autoInstitution.getInstitutionName();
		tmsHomePage.selectInstitute(instituteName, institutionId);
		String className = "class" + dbService.sig(3);
		report.report("Class name is: " + className);
		tmsHomePage.swithchToMainFrame();
		tmsHomePage.enterClassName(className);
		tmsHomePage.clickOnAddClass();
		startStep("Validate that class was added");
		tmsHomePage.checkClassNameIsDisplayed(className);
		pageHelper.checkClassWasCreated(className, institutionId);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
