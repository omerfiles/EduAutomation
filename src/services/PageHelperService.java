package services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.system.SystemObjectImpl;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import Enums.AutoParams;
import Enums.ByTypes;
import Objects.AutoInstitution;
import Objects.Course;
import Objects.CourseUnit;
import Objects.Recording;
import Objects.SchoolAdmin;
import Objects.Student;
import Objects.Supervisor;
import Objects.Teacher;
import Objects.UnitComponent;
import Objects.UserObject;

@Service
public class PageHelperService extends SystemObjectImpl {

	GenericWebDriver webDriver;

	@Autowired
	Configuration configuration;
	@Autowired
	TextService textService;

	@Autowired
	DbService dbService;

	TestResultService testResultService;

	// @Autowired
	// AudioService audioService;

	List<Course> courses = null;
	List<Recording> recordings = null;
	private boolean edoLogoutNeeded;
	private boolean tmsLogoutNeeded;
	private AutoInstitution autoInstitution;
	private Student student;
	private Teacher teacher;
	private Supervisor supervisor;
	private UserObject schoolAdmin;

	private String sutUrl;

	public PageHelperService() {

	}

	public void init(GenericWebDriver webDriver,
			AutoInstitution autoInstitution, TestResultService testResultService)
			throws Exception {
		this.testResultService = testResultService;
		this.webDriver = webDriver;
		this.autoInstitution = autoInstitution;
		courses = loadCoursedDetailsFromCsv();
		recordings = loadRecordings();
		student = new Student();
		teacher = new Teacher();
		supervisor = new Supervisor();
		schoolAdmin = new UserObject();

		// check if student parameter is in maven command line
		if (System.getProperty("student") != null) {
			student.setUserName(System.getProperty("student"));
		} else {
			student.setUserName(configuration.getProperty("student.user.name"));
		}
		student.setPassword(configuration.getProperty("student.user.password"));

		if (System.getProperty("teacher") != null) {
			teacher.setUserName(System.getProperty("teacher"));
		} else {
			teacher.setUserName(configuration.getProperty("teacher.username"));
		}
		teacher.setPassword(configuration.getProperty("teacher.password"));

		supervisor.setUserName(configuration.getProperty("supervisor.user"));
		schoolAdmin.setUserName(configuration.getProperty("shcoolAdmin.user"));

	}

	public EdoHomePage loginAsStudent(Student student) throws Exception {
		this.student = student;
		return loginAsStudent();
	}

	public EdoHomePage loginAsStudent() throws Exception {

		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		// TODO - check if there is DB access
		setUserLoginToNull(dbService.getUserIdByUserName(student.getUserName(),
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		// edoHomePage.waitForPageToLoad();
		edoLogoutNeeded = true;
		webDriver.closeAlertByAccept();
		Thread.sleep(2000);
		return edoHomePage;
	}

	public EdoHomePage loginAsTeacher() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());

		teacher.setPassword(configuration.getProperty("teacher.password"));
		setUserLoginToNull(dbService.getUserIdByUserName(teacher.getUserName(),
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(teacher);
		edoHomePage.waitForPageToLoad();

		return edoHomePage;
	}

	public EdoHomePage loginAsSupervisor() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		supervisor.setPassword(configuration.getProperty("supervisor.pass"));
		setUserLoginToNull(dbService.getUserIdByUserName(
				supervisor.getUserName(), autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(supervisor);
		edoHomePage.waitForPageToLoad();

		return edoHomePage;

	}

	public EdoHomePage loginAsSchoolAdmin() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		schoolAdmin.setPassword(configuration.getProperty("shcoolAdmin.pass"));
		setUserLoginToNull(dbService.getUserIdByUserName(
				schoolAdmin.getUserName(), autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(schoolAdmin);
		edoHomePage.waitForPageToLoad();

		return edoHomePage;

	}

	public TmsHomePage loginToTmsAsAdmin() throws Exception {
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver,
				testResultService);
		tmsLoginPage.OpenPage(getTmsUrl());
		SchoolAdmin schoolAdmin = new SchoolAdmin();
		schoolAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		schoolAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(schoolAdmin);
		tmsHomePage.waitForPageToLoad();
		tmsLogoutNeeded = true;
		return tmsHomePage;

	}

	public String getSutAndSubDomain() {
		String str = configuration.getAutomationParam(
				AutoParams.sutUrl.toString(), AutoParams.sutUrl.toString())
				+ "//" + configuration.getProperty("institutaion.subdomain");

		return str;

	}

	public String getTmsUrl() {
		return configuration.getProperty("tms.url");
	}

	public List<Course> loadCoursedDetailsFromCsv() throws Exception {
		// "files/csvFiles/Courses.csv"
		String filepath = configuration.getProperty("coursesFilePath");
		if (filepath == null) {
			filepath = "files/csvFiles/Courses.csv";
		}
		return loadCoursedDetailsFromCsv(filepath);
	}

	public List<Course> loadCoursedDetailsFromCsv(String filePath)
			throws Exception {
		List<String[]> courses = textService.getStr2dimArrFromCsv(filePath);
		List<Course> coursesList = new ArrayList<Course>();
		for (int i = 0; i < courses.size(); i++) {
			Course course = new Course();
			course.setName(courses.get(i)[0]);

			CourseUnit courseUnit = new CourseUnit();
			courseUnit.setName(courses.get(i)[1]);

			UnitComponent unitComponent = new UnitComponent();
			unitComponent.setName(courses.get(i)[2]);
			unitComponent.setStageNumber(courses.get(i)[3]);

			courseUnit.addUnitComponent(unitComponent);

			course.AddUnit(courseUnit);

			coursesList.add(course);

			// courseUnit.setUnitComponent(unitComponent);

		}
		return coursesList;

	}

	public List<Course> getCourses() {
		return courses;
	}

	public Course initCouse(int courseId) {
		Course course = new Course();
		course.setName(courses.get(courseId).getName());
		course.setCourseUnit(courses.get(courseId).getCourseUnits().get(0)
				.getName());
		course.setUnitComponent(courses.get(courseId).getCourseUnits().get(0)
				.getUnitComponent().get(0).getName());
		course.setComponentStage(Integer.valueOf(courses.get(courseId)
				.getCourseUnits().get(0).getUnitComponent().get(0)
				.getStageNumber()));
		return course;
	}

	public void checkClassWasCreated(String className, String institutionId)
			throws Exception {
		String sql = "select * from Class where Name='" + className
				+ "' and institutionId=" + institutionId;
		dbService.getStringFromQuery(sql);
	}

	public void startRecording(String fileName) throws Exception {
		// TODO 1. click on the recored button
		// audioService.sendSoundToVirtualMic(new File(fileName));
	}

	public List<Recording> loadRecordings() throws Exception {
		List<String[]> recordingsCsv = textService
				.getStr2dimArrFromCsv("files/csvFiles/recordingResults.csv");
		List<Recording> recordings = new ArrayList<Recording>();
		for (int i = 0; i < recordingsCsv.size(); i++) {
			Recording recording = new Recording();
			recording.setId(recordingsCsv.get(i)[0]);

			String[] Wlevels = textService.splitStringToArray(
					recordingsCsv.get(i)[1], "\\|");
			String[] Slevels = textService.splitStringToArray(
					recordingsCsv.get(i)[2], "\\|");
			String[] files = textService.splitStringToArray(
					recordingsCsv.get(i)[3], "\\|");

			List<Integer> SL = new ArrayList<Integer>();
			List<String[]> WL = new ArrayList<String[]>();
			List<File> recFiles = new ArrayList<File>();

			for (int j = 0; j < Wlevels.length; j++) {
				WL.add(textService.splitStringToArray(Wlevels[j]));
				SL.add(Integer.valueOf(Slevels[j]));
				recFiles.add(new File("files/audioFiles/" + files[j]));
			}
			recording.setWL(WL);
			recording.setSL(SL);
			recording.setRecordingFiles(recFiles);

			recordings.add(recording);

		}

		return recordings;

	}

	public List<Recording> getRecordings() {
		return recordings;
	}

	public void logOut() throws Exception {
		webDriver.waitForElement("Log Out", ByTypes.linkText).click();
		// webDriver.switchToFrame("lastAct");
		webDriver.switchToFrame(webDriver.waitForElement(
				"//iframe[contains(@src,'LogOut')]", ByTypes.xpath));
		// webDriver.closeAlertByAccept();
		webDriver.waitForElement("btnOk", ByTypes.id).click();

	}

	public boolean isLogoutNeeded() {
		return edoLogoutNeeded;
	}

	public void setLogoutNeeded(boolean logoutNeeded) {
		this.edoLogoutNeeded = logoutNeeded;
	}

	public void setUserLoginToNull(String id) throws Exception {
		String sql = "Update users set logedin = null where userid=" + id;
		dbService.runDeleteUpdateSql(sql);
	}

	public String[] getClassesForImport(String institutionId) throws Exception {
		String sql = "select top 1 Name from class where institutionId="
				+ institutionId + "  order by Name ";
		List<String> classes = dbService.getArrayListFromQuery(sql, 5);
		String[] classesStr = classes.toArray(new String[classes.size()]);
		return classesStr;
	}

	public String[] getStudentsForExporte(String objectName, int count,
			String institutionId, String by) throws Exception {
		String sql = "select top " + count + " " + by + " from " + objectName
				+ " where institutionId=" + institutionId + " order by " + by;
		List<String> objects = dbService.getArrayListFromQuery(sql, 5);
		String[] objectsArr = objects.toArray(new String[objects.size()]);
		return objectsArr;
	}

	public String[] convertStudentIdsToNames(String[] students)
			throws Exception {
		String[] studentNames = new String[students.length];
		for (int i = 0; i < students.length; i++) {
			studentNames[i] = dbService.getUserNameById(students[i],
					autoInstitution.getInstitutionId());
		}
		return studentNames;
	}

	public List shuffleList(List list) throws Exception {

		long seed = System.nanoTime();
		Collections.shuffle(list, new Random(seed));
		return list;

	}

	public Student getStudent() {
		return student;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void calculateSLbyWL(String[] WL, String SL) {
		Double wordLevels = 0.0;
		for (int i = 0; i < WL.length; i++) {
			wordLevels += Integer.valueOf(WL[i]);
		}
		wordLevels = wordLevels / WL.length;
		wordLevels = Math.ceil(wordLevels);
		int wl = wordLevels.intValue();
		System.out.println("Rounded avg: " + wl);

		// System.out.println("testResultService:"+testResultService.toString());
		testResultService.assertEquals(String.valueOf(wl), SL,
				"Sentence level do not match");

	}

	public void addStudent(String studentName) throws Exception {
		// String studentName = "student" + dbService.sig(6);
		String studentPassword = "12345";
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver,
				testResultService);
		UserObject tmsAdmin = new UserObject();
		tmsAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		tmsAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		tmsLoginPage.OpenPage(configuration.getProperty("tms.url"));
		TmsHomePage tmsHomePage = tmsLoginPage.Login(tmsAdmin);
		tmsHomePage.waitForPageToLoad();
		report.stopLevel();

		// report.startLevel("Go to students section and select the institute",
		// EnumReportLevel.CurrentPlace);
		tmsHomePage.clickOnStudents();
		Thread.sleep(2000);
		String institutionId = configuration.getProperty("institution.id");
		String instituteName = dbService.getInstituteNameById(institutionId);
		tmsHomePage.selectInstitute(instituteName, institutionId, false, true);
		Thread.sleep(3000);
		tmsHomePage.selectClass(configuration.getProperty("classname"));

		report.stopLevel();

		// report.startLevel("Enter new student details",
		// EnumReportLevel.CurrentPlace);

		tmsHomePage.enterStudentDetails(studentName);
		String userId = dbService.getUserIdByUserName(studentName,
				autoInstitution.getInstitutionId());
		tmsHomePage.enterStudentPassword(userId, studentPassword);
	}

}
