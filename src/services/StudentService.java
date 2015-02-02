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
		List<StudentProgress> list = new ArrayList<StudentProgress>();
		for (int i = 0; i < studentIds.length; i++) {
			list.addAll(getStudentProgress(studentIds[i], type));
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
			InstallationType type) throws Exception {
		List<StudentProgress> progressList = new ArrayList<StudentProgress>();
		String sql = SQL_FOR_PROGRESS + studentId;
		try {
			if (type == InstallationType.Offline) {
				dbService.setUseOfflineDB(true);

			}
			List<String[]> progressRecords = dbService.getStringListFromQuery(
					sql, 1, 4);
			if (progressRecords.size() > 0) {
				for (int i = 0; i < progressRecords.size(); i++) {

					System.out.println(progressRecords.get(i)[0]);
					System.out.println(progressRecords.get(i)[1]);
					System.out.println(progressRecords.get(i)[2]);
					System.out.println(progressRecords.get(i)[3]);

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
					+ institutionId;
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
		return str;
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
}
