package services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.system.SystemObjectImpl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.openqa.selenium.UnhandledAlertException;
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
		if (!configuration.getAutomationParam("coursesCsvFileName",
				"coursesCsvFileName").equals("")) {
			courses = loadCoursedDetailsFromCsv("files/csvFiles/"
					+ configuration.getProperty("coursesCsvFileName"));
		} else {
			courses = loadCoursedDetailsFromCsv();
		}

		recordings = loadRecordings();
		student = new Student();
		teacher = new Teacher();
		supervisor = new Supervisor();
		schoolAdmin = new UserObject();

		// check if student parameter is in maven command line
		student.setUserName(configuration.getAutomationParam("student",
				"studentCMD"));
		// if (System.getProperty("student") != null) {
		// student.setUserName(System.getProperty("student"));
		// } else {
		// student.setUserName(configuration.getProperty("student.user.name"));
		// }
		student.setPassword(configuration.getProperty("student.user.password"));

		teacher.setUserName(configuration.getAutomationParam("teacher.username", "teacher"));
		teacher.setPassword(configuration.getProperty("teacher.password"));

		supervisor.setUserName(configuration.getProperty("supervisor.user"));
		System.out.println("School admin from properties file is: "
				+ configuration.getProperty("shcoolAdmin.user"));
		schoolAdmin.setUserName(configuration.getProperty("schoolAdmin.user"));

	}

	public EdoHomePage loginAsStudent(Student student) throws Exception {
		this.student = student;
		return loginAsStudent();
	}

	public EdoHomePage loginAsStudent() throws Exception {

		EdoHomePage edoHomePage = null;
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		try {

			edoLoginPage.OpenPage(getSutAndSubDomain());
			// TODO - check if there is DB access
			setUserLoginToNull(dbService.getUserIdByUserName(
					student.getUserName(), autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(student);
			// edoHomePage.waitForPageToLoad();
			edoLogoutNeeded = true;
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.getUnexpectedAlertDetails();
		} finally {
			webDriver.closeAlertByAccept();
		}

		return edoHomePage;
	}

	public EdoHomePage loginAsTeacher() throws Exception {
		return loginAsTeacher(teacher.getUserName(),
				configuration.getProperty("institutaion.subdomain"));
	}

	public EdoHomePage loginAsTeacher(String teacherUserName,
			String instSubDomain) throws Exception {
		teacher.setUserName(teacherUserName);
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		edoLoginPage.OpenPage(getSutAndSubDomain(instSubDomain));

		teacher.setPassword(configuration.getProperty("teacher.password"));
		setUserLoginToNull(dbService.getUserIdByUserName(teacherUserName,
				autoInstitution.getInstitutionId()));

		EdoHomePage edoHomePage = edoLoginPage.login(teacher);
		// edoHomePage.waitForPageToLoad();
		webDriver.closeAlertByAccept();
		return edoHomePage;
	}

	public EdoHomePage loginAsSupervisor() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		EdoHomePage edoHomePage = null;
		try {
			edoLoginPage.OpenPage(getSutAndSubDomain());
			supervisor
					.setPassword(configuration.getProperty("supervisor.pass"));
			setUserLoginToNull(dbService.getUserIdByUserName(
					supervisor.getUserName(),
					autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(supervisor);
			edoHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		return edoHomePage;

	}

	public EdoHomePage loginAsSchoolAdmin() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver,
				testResultService);
		EdoHomePage edoHomePage = null;
		try {
			edoLoginPage.OpenPage(getSutAndSubDomain());
			schoolAdmin.setPassword(configuration
					.getProperty("schoolAdmin.pass"));
			setUserLoginToNull(dbService.getUserIdByUserName(
					schoolAdmin.getUserName(),
					autoInstitution.getInstitutionId()));
			edoHomePage = edoLoginPage.login(schoolAdmin);
			edoHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		return edoHomePage;

	}

	public TmsHomePage loginToTmsAsAdmin() throws Exception {
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver,
				testResultService);
		tmsLoginPage.OpenPage(getTmsUrl());
		TmsHomePage tmsHomePage = null;
		try {
			SchoolAdmin schoolAdmin = new SchoolAdmin();
			schoolAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
			schoolAdmin.setPassword(configuration
					.getProperty("tmsadmin.password"));
			tmsHomePage = tmsLoginPage.Login(schoolAdmin);
			tmsHomePage.waitForPageToLoad();
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			webDriver.closeAlertByAccept();
		}
		webDriver.closeAlertByAccept();
		tmsLogoutNeeded = true;
		return tmsHomePage;

	}

	public String getSutAndSubDomain() {
		return getSutAndSubDomain(configuration
				.getProperty("institutaion.subdomain"));
	}

	public String getSutAndSubDomain(String subDomanin) {
		String str = configuration.getAutomationParam(
				AutoParams.sutUrl.toString(), AutoParams.sutUrl.toString())
				+ "//" + subDomanin;
		System.out.println("SUT is: " + str);
		return str;

	}

	public String getTmsUrl() {
		return configuration.getProperty("tms.url");
	}

	public List<Course> loadCoursedDetailsFromCsv() throws Exception {
		// "files/csvFiles/Courses.csv"
		// String filepath = configuration.getAutomationParam("coursesFilePath",
		// null);
		// if (filepath == null) {
		String filepath = "files/csvFiles/Courses.csv";
		// }
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
		// webDriver.waitForElement("Log Out", ByTypes.linkText).click();
		// // webDriver.switchToFrame("lastAct");
		// webDriver.switchToFrame(webDriver.waitForElement(
		// "//iframe[contains(@src,'LogOut')]", ByTypes.xpath));
		// // webDriver.closeAlertByAccept();
		// webDriver.waitForElement("btnOk", ByTypes.id).click();

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
		addStudent(studentName, configuration.getProperty("classname"));
	}

	public void addStudent(String studentName, String className)
			throws Exception {


		// ************Using API to create the user

		createUserUsingApi(configuration.getProperty("sut.url"), studentName,
				studentName, studentName, "12345",
				autoInstitution.getInstitutionId(), className);
	}

	public void createUserUsingApi(String sut, String userName, String fname,
			String lname, String pass, String instId, String className)
			throws Exception {

		String request = sut + "/api/template/InsertUser.aspx?InstId=" + instId
				+ "&ClassName=" + className + "&UserName=" + userName
				+ "&FirstName=" + fname + "&LastName=" + lname + "&Password="
				+ pass + "&Email=test40@edusoft.co.il";

		NetService netService = new NetService();
		netService.sentHttpRequest(request);

	}

	public boolean[] randomizeCorrectAndIncorrectAnswers(int answersNumber) {
		int rand = dbService.getRandonNumber(1, 1000);
		System.out.println("Randon number is: " + rand);
		boolean[] questions = new boolean[answersNumber];

		// set all answersToBeTrue
		for (int i = 0; i < answersNumber; i++) {
			questions[i] = true;
		}

		if (rand < 200) {
			// test score will be 0
			for (int i = 0; i < questions.length ; i++) {
				questions[i] = false;
			}

		} else if (rand > 201 && rand < 400) {
			// test score will be 25
			for (int i = 0; i < questions.length * 0.75; i++) {
				questions[i] = false;
			}
		} else if (rand > 401 && rand < 600) {
			// test score will be 50
			for (int i = 0; i < questions.length * 0.5; i++) {
				questions[i] = false;
			}
			// test score will be 75
		} else if (rand > 601 && rand < 800) {
			for (int i = 0; i < questions.length * 0.25; i++) {
				questions[i] = false;
			}
		}
		else if (rand > 801 && rand <= 1000) {
			//test score will be 100
			
		}
		return questions;
	}

	public void waitForDateTime(DateTime timeToWaitFor) throws InterruptedException {
		LocalDateTime localDateTime = new LocalDateTime();  
		
		Duration myDuration = new Duration(timeToWaitFor.toDateTime(),
				localDateTime.toDateTime());
		System.out.println("Seconds left: " + myDuration.getStandardSeconds());
		Thread.sleep(myDuration.getMillis()+60000);
	}
}
