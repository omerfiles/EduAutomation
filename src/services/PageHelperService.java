package services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jsystem.framework.system.SystemObjectImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import Enums.ByTypes;
import Objects.AutoInstitution;
import Objects.Course;
import Objects.CourseUnit;
import Objects.Recording;
import Objects.SchoolAdmin;
import Objects.Student;
import Objects.Teacher;
import Objects.UnitComponent;

@Service
public class PageHelperService extends SystemObjectImpl {

	GenericWebDriver webDriver;

	@Autowired
	Configuration configuration;
	@Autowired
	TextService textService;

	@Autowired
	DbService dbService;

	@Autowired
	AudioService audioService;

	List<Course> courses = null;
	List<Recording> recordings = null;
	private boolean edoLogoutNeeded;
	private boolean tmsLogoutNeeded;
	private AutoInstitution autoInstitution;

	public PageHelperService() {

	}

	public void init(GenericWebDriver webDriver, AutoInstitution autoInstitution)
			throws Exception {
		this.webDriver = webDriver;
		this.autoInstitution = autoInstitution;
		courses = loadCoursedDetailsFromCsv();
		recordings = loadRecordings();

	}

	public EdoHomePage loginAsStudent() throws Exception {

		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Student student = new Student();
		student.setUserName(configuration.getProperty("student.user.name"));
		student.setPassword(configuration.getProperty("student.user.password"));
		//setUserLoginToNull(dbService.getUserIdByUserName(student.getUserName(),
			//	autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(student);
//		edoHomePage.waitForPageToLoad();
		edoLogoutNeeded = true;
		return edoHomePage;
	}

	public EdoHomePage loginAsTeacher() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Teacher teacher = new Teacher();
		teacher.setUserName(configuration.getProperty("teacher.username"));
		teacher.setPassword(configuration.getProperty("teacher.password"));
		setUserLoginToNull(dbService.getUserIdByUserName(teacher.getUserName(),
				autoInstitution.getInstitutionId()));
		EdoHomePage edoHomePage = edoLoginPage.login(teacher);
		edoHomePage.waitForPageToLoad();

		return edoHomePage;
	}

	public TmsHomePage loginToTmsAsAdmin() throws Exception {
		TmsLoginPage tmsLoginPage = new TmsLoginPage(webDriver);
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
		return configuration.getProperty("sut.url") + "//"
				+ configuration.getProperty("institutaion.subdomain");

	}

	public String getTmsUrl() {
		return configuration.getProperty("tms.url");
	}

	public List<Course> loadCoursedDetailsFromCsv() throws Exception {
		List<String[]> courses = textService
				.getStr2dimArrFromCsv("files/csvFiles/Courses.csv");
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
		audioService.sendSoundToVirtualMic(new File(fileName));
	}

	public List<Recording> loadRecordings() throws Exception {
		List<String[]> recordingsCsv = textService
				.getStr2dimArrFromCsv("files/csvFiles/recordingResults.csv");
		List<Recording> recordings = new ArrayList<Recording>();
		for (int i = 0; i < recordingsCsv.size(); i++) {
			Recording recording = new Recording();
			recording.setId(recordingsCsv.get(i)[0]);
			recording.setWordsScores(textService
					.splitStringToArray(recordingsCsv.get(i)[3]));
			recording.setRecordingFile(new File(recordingsCsv.get(i)[4]));
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
	
	public List shuffleList(List list)throws Exception{
		
		long seed=System.nanoTime();
		Collections.shuffle(list,new Random(seed));
		return list;
		
	}
	
	

}
