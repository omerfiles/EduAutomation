package services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Enums.InstallationType;
import Enums.StudentObjectType;
import Objects.StudentObject;
import Objects.StudentProgress;
import Objects.StudentTest;

@Service("StudentService")
public class StudentService extends GenericService {

	private static final String SQL_FOR_PROGRESS = "select UserId,CourseId,ItemId,LastUpdate from  progress where userId=";
	private static final String SQL_FOR_TESTS = "select  UserId, ComponentSubComponentId,CourseId,Grade,LastUpdate,Average,TimesTaken from TestResults  where userId=";

	@Autowired
	DbService dbService;

	@Autowired
	TextService textService;

	@Autowired
	Configuration configuration;

	@Autowired
	TestResultService testResultService;

	@Autowired
	Reporter report;

	public List<StudentObject> getStudentObjectsList(String studentId,
			InstallationType type, StudentObjectType objectType) {

		List<StudentObject> studentObjects = null;

		String sql = null;
		int numOfColumns = 0;

		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);

			}

			switch (objectType) {
			case Progress:
				sql = SQL_FOR_PROGRESS + studentId;
				numOfColumns = 4;
				break;
			case Tests:

			default:
				break;
			}

			List<String[]> records = dbService.getStringListFromQuery(sql, 1,
					numOfColumns);
			String[] columns = new String[records.get(0).length];

			for (int i = 0; i < records.size(); i++) {
				String[] str = new String[columns.length];
				for (int j = 0; j < columns.length; j++) {
					str[j] = records.get(i)[j];
				}

				StudentObject studentObject = new StudentObject(str);
				studentObjects.add(studentObject);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}
		return studentObjects;

	}

	// public List<StudentObject> getStudentProgress(String studentId,
	// InstallationType type){
	// return getStudentObjectsList(studentId, type,
	// StudentObjectType.Progress);
	// }

	public List<StudentTest> getMultipleStudentsTests(String[] studentIds,
			InstallationType type) throws Exception {
		List<StudentTest> list = new ArrayList<StudentTest>();
		for (int i = 0; i < studentIds.length; i++) {
			list.addAll(getStudentTests(studentIds[i], type));
		}
		return list;
	}

	public List<StudentProgress> getMultipleStudentsProgress(
			String[] studentIds, InstallationType type) throws Exception {
		return getMultipleStudentsProgress(studentIds, type,
				configuration.getProperty("institution.id"));
	}

	public List<StudentProgress> getMultipleStudentsProgress(
			String[] studentIds, InstallationType type, String institutionId)
			throws Exception {
		List<StudentProgress> list = new ArrayList<StudentProgress>();
		for (int i = 0; i < studentIds.length; i++) {
			List<StudentProgress> studentProgress = getStudentProgress(
					studentIds[i], type, institutionId);
			if (studentProgress.size() > 0) {
				list.addAll(studentProgress);
			}

		}
		return list;
	}

	public List<StudentTest> getStudentTests(String studentId,
			InstallationType type) {

		List<StudentTest> studentTests = new ArrayList<StudentTest>();
		String sql = SQL_FOR_TESTS + studentId;
		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);

			}

			List<String[]> testsRecords = dbService.getStringListFromQuery(sql,
					1, 7);
			if (testsRecords.size() > 0) {
				for (int i = 0; i < testsRecords.size(); i++) {

					DateTimeFormatter formatter = DateTimeFormat
							.forPattern("yyyy-MM-dd HH:mm:ss.S");
					DateTime dt = formatter
							.parseDateTime(testsRecords.get(i)[4]);

					StudentTest studentTest = new StudentTest(
							testsRecords.get(i)[0], testsRecords.get(i)[1],
							testsRecords.get(i)[2], testsRecords.get(i)[3], dt,
							testsRecords.get(i)[5], testsRecords.get(i)[6]);
					studentTests.add(studentTest);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}
		return studentTests;

	}

	public List<StudentProgress> getStudentProgress(String studentId,
			InstallationType type, String institutionId) throws Exception {
		List<StudentProgress> progressList = new ArrayList<StudentProgress>();
		String sql = SQL_FOR_PROGRESS + studentId;
		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
				sql = sql + " and Synchronized is not null";

			}
			// if(type==InstallationType.Online){
			// sql = sql + " and institutionId="+institutionId;
			// }
			List<String[]> progressRecords = dbService.getStringListFromQuery(
					sql, 1, 4);
			if (progressRecords.size() > 0) {
				for (int i = 0; i < progressRecords.size(); i++) {

					// System.out.println(progressRecords.get(i)[0]);
					// System.out.println(progressRecords.get(i)[1]);
					// System.out.println(progressRecords.get(i)[2]);
					// System.out.println(progressRecords.get(i)[3]);

					DateTimeFormatter formatter = DateTimeFormat
							.forPattern("yyyy-MM-dd HH:mm:ss.S");

					DateTime dt = formatter.parseDateTime(progressRecords
							.get(i)[3]);

					StudentProgress progress = new StudentProgress(
							progressRecords.get(i)[0],
							progressRecords.get(i)[1],
							progressRecords.get(i)[2], dt);

					progressList.add(progress);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);

			}
		}

		return progressList;

	}

	public List<String[]> getStudentTestListIntoArrayList(List<StudentTest> list) {
		List<String[]> arrList = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {
			arrList.add(list.get(i).getStringArr());
		}
		return arrList;
	}

	public List<String[]> getStudentProgressListIntoArrayList(
			List<StudentProgress> list) {
		List<String[]> arrList = new ArrayList<String[]>();
		for (int i = 0; i < list.size(); i++) {
			arrList.add(list.get(i).getStringArr());
		}
		return arrList;
	}

	public List<StudentProgress> getMultipleStudentsProgressFromCsvFile(
			String csvPath) throws Exception {
		List<StudentProgress> list = new ArrayList<StudentProgress>();
		List<String[]> csvList = textService.getStr2dimArrFromCsv(csvPath);
		for (int i = 0; i < csvList.size(); i++) {
			StudentProgress studentProgress = new StudentProgress(
					csvList.get(i)[0], csvList.get(i)[1], csvList.get(i)[2],
					csvList.get(i)[3], "yyyy-MM-dd HH:mm:ss");
			list.add(studentProgress);
		}
		return list;
	}

	public List<StudentTest> getMultipleStudentsTestsFromCsvFile(String csvPath)
			throws Exception {
		List<StudentTest> list = new ArrayList<StudentTest>();

		List<String[]> csvList = textService.getStr2dimArrFromCsv(csvPath);
		for (int i = 0; i < csvList.size(); i++) {

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss.S");
			DateTime dt = formatter.parseDateTime(csvList.get(i)[4]);
			// DateTime lastUdate;

			StudentTest studentTest = new StudentTest(csvList.get(i)[0],
					csvList.get(i)[1], csvList.get(i)[2], csvList.get(i)[3],
					dt, csvList.get(i)[5], csvList.get(i)[6]);
			list.add(studentTest);
		}
		return list;
	}

	public String[] getInstitutionStudetns(InstallationType type,
			String institutionId) {
		if (type.equals(InstallationType.Offline)) {
			dbService.setUseOfflineDB(true);
		}
		List<String[]> list = new ArrayList<String[]>();
		String[] str = null;

		try {

			String sql = "select UserId from users where InstitutionId="
					+ institutionId
					+ " and UserTypeId=1 and FirstName not in('Admin','st1') ";
			list = dbService.getStringListFromQuery(sql, 1, 1);
			str = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				str[i] = list.get(i)[0];
			}

		} catch (Exception e) {

		} finally {
			if (type.equals(InstallationType.Offline)) {
				dbService.setUseOfflineDB(false);
			}
		}
		System.out.println("Num of students=" + str.length);
		return str;
	}

	public int getTestNumOfQuestions(String testId) throws Exception {
		String csvPath = "files/offline/peru/TestsQuestions.csv";
		List<String[]> testsList = textService.getStr2dimArrFromCsv(csvPath);
		int questions = 0;
		for (int i = 0; i < testsList.size(); i++) {
			if (testsList.get(i)[0].equals(testId)) {
				questions++;
			}
		}
		return questions;
	}

	public List<String> createAndRunProgressSqlRecordsForStudent(
			String studentId, boolean executeQuery) throws Exception {
		List<String> spList = new ArrayList<String>();
		List<String[]> coursesDetails = textService
				.getStr2dimArrFromCsv("files/offline/peru/PeruOfflineCoursesUnits.csv");
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
				+ studentId

				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
		for (int i = 0; i < coursesDetails.size(); i++) {
			// String sqlForAdd = sqlText;
			// sqlForAdd = sqlForAdd.replace("%courseId%",
			// coursesDetails.get(i)[0]);
			// sqlForAdd = sqlForAdd.replace("%itemId%",
			// coursesDetails.get(i)[1]);
			// //
			// // spList.add(sqlForAdd);
			// dbService.runStorePrecedure(sqlForAdd, executeQuery,false);
			createSingleProgressRecored(studentId, coursesDetails.get(i)[0],
					coursesDetails.get(i)[1], executeQuery);

		}
		return spList;

	}

	public void createSingleProgressRecored(String studentId, String courseId,
			String itemId, boolean executeQuery) throws Exception {
		String sqlText = "exec SetProgress @CourseId=" + courseId + ",@ItemId="
				+ itemId + ",@UserId=" + studentId;
		dbService.runStorePrecedure(sqlText, executeQuery, false);

	}

	public void createAndRunSetSubmitTestSqlRecordsForStudent(String studentId,
			boolean offlineDB) throws Exception {
		List<String[]> coursesDetails = textService
				.getStr2dimArrFromCsv("files/offline/peru/peruOfflineTests.csv");

		String sql = "exec SubmitTest @UserId="
				+ studentId
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks='%marks%',@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";

		for (int i = 0; i < coursesDetails.size(); i++) {
			int grade = dbService.getRandonNumber(0, 100);
			List<String> marks = generateMarks(getTestNumOfQuestions(coursesDetails
					.get(i)[1]));
			String sp = sql;
			sp = sp.replace("%unitId%", coursesDetails.get(i)[0]);
			sp = sp.replace("%compId%", coursesDetails.get(i)[1]);
			sp = sp.replace("%marks%", getStringFromMarks(marks));
			sp = sp.replace("%grade%", String.valueOf(calcGrade(marks)));

			// System.out.println(sp);
			dbService.runStorePrecedure(sp, offlineDB);
		}

	}

	private List<String> generateMarks(int testNumOfQuestions) {
		// TODO Auto-generated method stub
		List<String> marks = new ArrayList<String>();
		for (int i = 0; i < testNumOfQuestions; i++) {
			marks.add(String.valueOf(dbService.getRandonNumber(30, 100)));
		}
		return marks;
	}

	public String getStringFromMarks(List<String> marks) {
		String result = "";
		for (int i = 0; i < marks.size(); i++) {
			result += marks.get(i) + "|";
		}
		// result += "'";
		return result;
	}

	public void setProgress(String studentId, String courseId, String itemId)
			throws Exception {
		setProgress(studentId, courseId, itemId, InstallationType.Online);
	}

	public void setProgress(String studentId, String courseId, String itemId,
			InstallationType type) throws Exception {

		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
			}
			String sqlText = "exec SetProgress @CourseId=" + courseId
					+ ",@ItemId=" + itemId + ",@UserId=" + studentId
					+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
				dbService.runStorePrecedure(sqlText, true);
			} else {
				dbService.runStorePrecedure(sqlText, false);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}
	}

	public int calcGrade(List<String> marks) {
		int counter = marks.size();
		double sum = 0;
		for (int i = 0; i < marks.size(); i++) {
			sum += Double.valueOf(marks.get(i));
		}
		double result = sum / counter;

		result = Math.round(result);
		System.out.println(result);
		return (int) result;
		// int result = 0;
		// switch (counter) {
		// case 0:
		// result = 0;
		// case 1:
		// result = 20;
		// case 2:
		// result = 40;
		// case 3:
		// result = 60;
		// case 4:
		// result = 80;
		// case 5:
		// result = 100;
		// }
		//
		// return result;
	}

	public void checkStudentProgress(String studentId, String courseId,
			String itemId) throws Exception {
		checkStudentProgress(studentId, courseId, itemId,
				InstallationType.Online);
	}

	public void checkStudentProgress(String studentId, String courseId,
			String itemId, InstallationType installationType) throws Exception {
		try {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);
			}
			String sql = "select progressId from Progress where UserId="
					+ studentId + " and CourseId=" + courseId + " and ItemId="
					+ itemId;
			String result = dbService.getStringFromQuery(sql);

			assertNotNull(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}
	}

	public void createStudenCourseTestGrade(String studentID, String testId,
			String courseId, String unitId, String[] testComponents,
			String[] setIds, int[] numsofmarks,
			InstallationType installationType) throws Exception {

		boolean useOfflineDB = false;
		List<String> marks;
		// if (testComponents.length != grades.length) {
		// testResultService
		// .addFailTest(
		// "test components and grades arrays length is not the same. please check test inputes",
		// true, false);
		// }

		if (installationType == InstallationType.Offline) {
			dbService.setUseOfflineDB(true);
			useOfflineDB = true;
		}
		try {

			report.startLevel("set didTest");
			String startTestSP = "exec  StartExitTest " + studentID;
			dbService.runStorePrecedure(startTestSP, true);

			report.startLevel("set StartExitTest");
			String didTestTSP = "exec  SetDidTest " + studentID;
			if (useOfflineDB) {
				dbService.runStorePrecedure(didTestTSP, true, true);
			} else {
				dbService.runStorePrecedure(didTestTSP, true, true);
			}

			report.startLevel("Submit test and setExitTestGrades for all components");
			String submitTestSPbase = "exec SubmitTest @UserId=%userid%,@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks='%marks%',@SetId=%setIds%,@VisitedItems='%visitedItems%',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A2262317265616C743031222C22694964223A36313134352C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743032222C22694964223A36313134362C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743033222C22694964223A36313134372C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743034222C22694964223A36313134382C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743035222C22694964223A36313134392C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743036222C22694964223A36313135302C226954797065223A35312C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743037222C22694964223A36313135312C226954797065223A32332C226D223A302C227561223A5B5D7D2C7B2269436F6465223A2262317265616C743038222C22694964223A36313135322C226954797065223A35312C226D223A302C227561223A5B5D7D5D2C226D223A302C2274223A33323030307D,@TestTime=32000";
			String setExitTestGradeSpbase = "exec SetExitTestGrade %userid%,%testId%,%courseId%,%grade%";
			int avgGrade = 0;
			for (int i = 0; i < testComponents.length; i++) {
				marks = generateMarks(numsofmarks[i]);
				avgGrade += calcGrade(marks);
				String grade = String.valueOf(calcGrade(marks));
				report.startLevel("prepair and run submit test SP");
				String submitTestSp = submitTestSPbase;
				submitTestSp = submitTestSp.replace("%userid%", studentID);
				submitTestSp = submitTestSp.replace("%unitId%", unitId);
				submitTestSp = submitTestSp.replace("%grade%", grade);
				submitTestSp = submitTestSp.replace("%marks%",
						getStringFromMarks(marks));
				submitTestSp = submitTestSp.replace("%compId%",
						testComponents[i]);
				submitTestSp = submitTestSp.replace("%setIds%", setIds[i]);
				submitTestSp = submitTestSp.replace("%visitedItems%",
						generateVisitedItemsString(numsofmarks[i]));

				dbService.runStorePrecedure(submitTestSp, useOfflineDB);

				report.startLevel("prepair and run SetExitTestGrade SP");
				String SetExitTestGradeSP = setExitTestGradeSpbase;
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%userid%",
						studentID);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%testId%",
						testId);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%courseId%",
						courseId);
				SetExitTestGradeSP = SetExitTestGradeSP.replace("%grade%",
						grade);

				if (useOfflineDB) {
					dbService.runStorePrecedure(SetExitTestGradeSP,
							useOfflineDB, true);
				} else {
					dbService.runStorePrecedure(SetExitTestGradeSP,
							useOfflineDB);
				}

			}
			System.out.println("Expected grade in DB is: " + avgGrade
					/ testComponents.length);

		} catch (Exception e) {

		} finally {
			if (installationType == InstallationType.Offline) {
				dbService.setUseOfflineDB(false);
			}
		}

	}

	// public StudentTest
	// getTestresultByCompSubCompIdCourseIdAndStudentId(String subCompId,
	// String courseId, String UserId) throws Exception {
	// String sql = "select * from TestResults where CourseId=" + courseId
	// + " and UserId=" + UserId + " and ComponentSubComponentId="
	// + subCompId;
	//
	// List<String[]> result = dbService.getStringListFromQuery(sql, 1, 10);
	// StudentTest studentTest
	// }

	private String generateVisitedItemsString(int numbderOfItems) {
		// TODO Auto-generated method stub
		String str = "";
		for (int i = 1; i < numbderOfItems + 1; i++) {
			str += "[" + i + "]";
		}
		// str+="'";

		return str;
	}
}
