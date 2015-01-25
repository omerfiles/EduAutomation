package tests.edo.Erater;

import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pageObjects.EdoHomePage;
import pageObjects.EdoLoginPage;
import pageObjects.tms.TmsHomePage;
import Enums.ByTypes;
import Objects.Course;
import Objects.Student;
import Objects.Teacher;
import tests.misc.EdusoftWebTest;

public class EraterAddRemoveFeedbackComments extends EdusoftWebTest {

	List<Course> courses = null;
	List<String> writingIdForDelete = new ArrayList<String>();

	@Before
	public void setup() throws Exception {
		super.setup();
		courses = pageHelper.getCourses();
		report.startLevel("delete all student assignments",
				Reporter.EnumReportLevel.CurrentPlace);
		String userId = dbService.getUserIdByUserName(
				configuration.getStudentUserName(),
				autoInstitution.getInstitutionId());
		eraterService.deleteStudentAssignments(userId);
		report.stopLevel();
		report.startLevel("Set institution to 'Teacher first' settings",
				Reporter.EnumReportLevel.CurrentPlace);
		eraterService.setEraterTeacherLast();
		report.stopLevel();
	}

	
	
	

	@After
	public void tearDown() throws Exception {
		report.report("Delete writings");
		for (int i = 0; i < writingIdForDelete.size(); i++) {
			eraterService.deleteWritngFromDb(writingIdForDelete.get(i));
		}
		super.tearDown();
	}

}
