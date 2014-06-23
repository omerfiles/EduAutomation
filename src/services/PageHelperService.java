package services;

import java.util.ArrayList;
import java.util.List;

import jsystem.framework.system.SystemObjectImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import drivers.GenericWebDriver;
import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import pageObjects.tms.TmsLoginPage;
import Objects.Course;
import Objects.CourseUnit;
import Objects.SchoolAdmin;
import Objects.Student;
import Objects.UnitComponent;

@Service
public class PageHelperService extends SystemObjectImpl {

	GenericWebDriver webDriver;

	@Autowired
	Configuration configuration;
	@Autowired
	TextService textService;

	List<Course> courses = null;

	public PageHelperService() {

	}

	public void init(GenericWebDriver webDriver) throws Exception {
		this.webDriver = webDriver;
		courses = loadCoursedDetailsFromCsv();

	}

	public EdoHomePage loginAsStudent() throws Exception {
		EdoLoginPage edoLoginPage = new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		Student student = new Student();
		student.setUserName(configuration.getProperty("student.user.name"));
		student.setPassword(configuration.getProperty("student.user.password"));
		EdoHomePage edoHomePage = edoLoginPage.login(student);
		edoHomePage.waitForPageToLoad();
		return edoHomePage;
	}

	public TmsHomePage loginToTmsAsAdmin()throws Exception{
		TmsLoginPage tmsLoginPage=new TmsLoginPage(webDriver);
		tmsLoginPage.OpenPage(getTmsUrl());
		SchoolAdmin schoolAdmin=new SchoolAdmin();
		schoolAdmin.setUserName(configuration.getProperty("tmsadmin.user"));
		schoolAdmin.setPassword(configuration.getProperty("tmsadmin.password"));
		TmsHomePage tmsHomePage=tmsLoginPage.Login(schoolAdmin);
		tmsHomePage.waitForPageToLoad();
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

}
