package tests.offline.sync;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.InstallationType;
import Objects.StudentProgress;
import Objects.StudentTest;
import tests.misc.EdusoftBasicTest;
import tests.misc.EdusoftWebTest;

public class ImportExportTests extends EdusoftWebTest {

	String[] offlineStudents = new String[] { "91000010000004" };
	String[] onlineStudents = new String[] { "52319080000005" };

	String offlineTestCsvPathBeofreSync;
	String onlineTestCsvPathBeforeSync;

	String offlineProgressCsvPathBeforeSync;
	String onlineProgressCsvPathBeforeSync;

	List<StudentTest> offlineTestsFromDb;
	List<StudentTest> onlineTestsFromDb;
	List<StudentProgress> offlineProgressFromDb;
	List<StudentProgress> onlineProgressFromDb;

	@Before
	public void setup() throws Exception {
		super.setup();
		offlineTestCsvPathBeofreSync = "files/temp/offlineTests" + ".csv";
		onlineTestCsvPathBeforeSync = "files/temp/onlineTests" + ".csv";

		offlineProgressCsvPathBeforeSync = "files/temp/offlineProgress"
				+ ".csv";
		onlineProgressCsvPathBeforeSync = "files/temp/onlineProgress" + ".csv";

	}

	@Test
	public void testToRunBeforeSync() throws Exception {
		// this saves the DB's data (relevanet to the tested students) and saves
		// it into csv files
		getDBsDataBeforeSync();
	}

	@Test
	public void testRunValidationAfterImportSetOnline()
			throws Exception {

		// 0. - setup: create new student in online for each test
		String studentId = null;

		// String offlineCsvUpdateFile =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\input\\submitTest.csv";
		// String onlineCsvUpdateFile =
		// "\\\\storage\\QA\\Automation\\testData\\forOnline\\input\\submitTest.csv";

		// List<String[]> offlineCSVList = textService
		// .getStr2dimArrFromCsv(offlineCsvUpdateFile);
		// List<String[]> onlineeCSVList = textService
		// .getStr2dimArrFromCsv(onlineCsvUpdateFile);

		// 0.1 - load db's data before sync from csv files created during

		List<StudentProgress> offlineProgressFromCsvFile;
		List<StudentProgress> onlineeProgressFromCsvFile;
		List<StudentTest> offlineTestsFromCsvFile;
		List<StudentTest> onlineTestsFromCsvFile;

		offlineProgressFromCsvFile = studentService
				.getMultipleStudentsProgressFromCsvFile(offlineProgressCsvPathBeforeSync);
		onlineeProgressFromCsvFile = studentService
				.getMultipleStudentsProgressFromCsvFile(onlineProgressCsvPathBeforeSync);

		offlineTestsFromCsvFile = studentService
				.getMultipleStudentsTestsFromCsvFile(offlineTestCsvPathBeofreSync);

		onlineTestsFromCsvFile = studentService
				.getMultipleStudentsTestsFromCsvFile(onlineTestCsvPathBeforeSync);

		// testToRunBeforeSync
		// List<StudentProgress> offlineProgressBeforeSync = studentService
		// .getMultipleStudentsProgressFromCsvFile(offlineTestCsvPathBeofreSync);
		//
		// List<StudentTest> offlineStudentTestsBeforeSync = studentService
		// .getMultipleStudentsTestsFromCsvFile(offlineTestCsvPathBeofreSync);

		// 1. Set progress in Online DB using this SQL file:

		// 2. run the import process manually using this file>

		// 3. Run validation on the DB

		// 3.1 check that all progress from online is updated in offline(if
		// progress did not existed in offline

		List<StudentProgress> offlineProgress = studentService
				.getMultipleStudentsProgress(offlineStudents,
						InstallationType.Offline);

		List<StudentProgress> onlineProgress = studentService
				.getMultipleStudentsProgress(onlineStudents,
						InstallationType.Online);

		// if (offlineProgress.size() == onlineProgress.size()
		// && offlineProgress.containsAll(onlineProgress)) {
		// System.out.println("Lists are the same!");

		// 3.1.1 check that new tests are added

		// 3.1.2 check that test score is updated to highest

		// 3.1.3 check that latest test is updated as latest

		// 3.1.4 recalculate average

		// 3.1.5 check that Times taken is accumulated
		// }
		// 3.2 check that all old progress in offline is not deleted

		// 3.3 online/ offline progress check - newer progress should be updated
		// in offline

		// ///**** checking progress
		for (int i = 0; i < offlineProgress.size(); i++) {

			StudentProgress offlineStudentProgress = new StudentProgress(
					offlineProgress.get(i).getUserId(), offlineProgress.get(i)
							.getCourseId(), offlineProgress.get(i).getItemId(),
					offlineProgress.get(i).getLastUpdate());
			innerloop:
				for (int j = 0; j < onlineProgress.size(); j++) {

				StudentProgress onlineStudentProgress = new StudentProgress(
						onlineProgress.get(i).getUserId(), onlineProgress
								.get(i).getCourseId(), onlineProgress.get(i)
								.getItemId(), onlineProgress.get(i)
								.getLastUpdate());

				// check that offline progress exist in online after sync
				if (offlineStudentProgress.getUserId().equals(
						onlineStudentProgress.getUserId())
						&& offlineStudentProgress.getCourseId().equals(
								onlineStudentProgress.getCourseId())
						&& offlineStudentProgress.getItemId().equals(
								onlineStudentProgress.getItemId())) {
					System.out.println("progress found in online DB: "
							+ offlineStudentProgress.getStringArr());

					DateTime offlineLastUpdate = getLastUpdateFromProgressList(
							offlineStudentProgress.getUserId(),
							offlineStudentProgress.getCourseId(),
							offlineStudentProgress.getItemId(),
							offlineProgressFromCsvFile);

					DateTime onlineLastUpdate = getLastUpdateFromProgressList(
							offlineStudentProgress.getUserId(),
							offlineStudentProgress.getCourseId(),
							offlineStudentProgress.getItemId(),
							onlineeProgressFromCsvFile);

					if (offlineLastUpdate.isAfter(onlineLastUpdate)) {
						// offline progress is laests
						testResultService.assertEquals(
								true,
								offlineStudentProgress.getLastUpdate().equals(
										offlineLastUpdate));

						testResultService.assertEquals(
								true,
								offlineStudentProgress.getLastUpdate().equals(
										onlineStudentProgress.getLastUpdate()));

					} else {
						// online progress is latest
						testResultService
								.assertEquals(
										true,
										offlineStudentProgress.getLastUpdate()
												.equals(onlineLastUpdate),
										"Online lastupdate is not the latest: "
												+ offlineStudentProgress
														.getStringArr());
						testResultService
								.assertEquals(
										true,
										offlineStudentProgress.getLastUpdate()
												.equals(onlineStudentProgress
														.getLastUpdate()),
										"online and offline last update do not match. online progress: "
												+ onlineStudentProgress
														.getStringArr()
												+ ". offline progress: "
												+ offlineStudentProgress
														.getStringArr());
					}

					break innerloop;
				} else {
					System.out
							.println("Offline progress not found. progress not found:"
									+  offlineStudentProgress.toString());
				}
				// check that lastupdate is latests

			}

		}

		// ************ end of checking progress

		// 4.0 - check tests grades

		List<StudentTest> offlineTests = studentService
				.getMultipleStudentsTests(offlineStudents,
						InstallationType.Offline);

		List<StudentTest> onlineTests = studentService
				.getMultipleStudentsTests(onlineStudents,
						InstallationType.Online);

		// 4.1.1 check that new tests are added
		System.out.println("Iterate on offline tests");
		// //************** checking tests **********************
		for (int i = 0; i < offlineTests.size(); i++) {
			// check if new test in online
			StudentTest offlineTest = new StudentTest(onlineTests.get(i)
					.getUserId(), onlineTests.get(i)
					.getComponentSubComponentId(), onlineTests.get(i)
					.getCourseId(), onlineTests.get(i).getGrade(), onlineTests
					.get(i).getLastUpdate(), onlineTests.get(i).getAvarage(),
					onlineTests.get(i).getTimesTaken());

			// check if offline test in online
			innerloop2: 
				for (int j = 0; j < onlineTests.size(); j++) {

				StudentTest onlineTest = new StudentTest(onlineTests.get(j)
						.getUserId(), onlineTests.get(j)
						.getComponentSubComponentId(), onlineTests.get(j)
						.getCourseId(), onlineTests.get(j).getGrade(),
						onlineTests.get(j).getLastUpdate(), onlineTests.get(j)
								.getAvarage(), onlineTests.get(j)
								.getTimesTaken());

				if (offlineTest.getComponentSubComponentId().equals(
						onlineTest.getComponentSubComponentId())
						&& offlineTest.getCourseId().equals(
								onlineTest.getCourseId())) {
					System.out.println("Test found");

					// 4.1.2 check that test score is updated to highest
					int onlineGrade = Integer.parseInt(onlineTest.getGrade());
					int offlineGrade = Integer.parseInt(offlineTest.getGrade());
					testResultService.assertEquals(onlineGrade, offlineGrade,
							"Grade do not match for course/compId: "
									+ onlineTest.getCourseId() + " "
									+ onlineTest.getComponentSubComponentId());
					// 4.1.2.1 - get highest score
					int OfflineGradeFromDbBeforeSync = Integer
							.valueOf(GetGradeFromList(offlineTest.getUserId(),
									offlineTest.getCourseId(),
									offlineTest.getComponentSubComponentId(),
									offlineTestsFromCsvFile));

					int onlineGradeFromDbBeforeSync = Integer
							.valueOf(GetGradeFromList(onlineTest.getUserId(),
									onlineTest.getCourseId(),
									onlineTest.getComponentSubComponentId(),
									onlineTestsFromCsvFile));
					if (OfflineGradeFromDbBeforeSync > onlineGradeFromDbBeforeSync) {
						testResultService.assertEquals(
								OfflineGradeFromDbBeforeSync, onlineGrade,
								"Online grade is not the max grade");
					} else {
						testResultService.assertEquals(
								onlineGradeFromDbBeforeSync, onlineGrade,
								"Online grade is not the max grade");
					}

					// 4.1.3 check that latest test is updated as latest
					DateTime lastUpdateFromOffline = getLastUpdateFromTestsList(
							offlineTest.getUserId(), offlineTest.getCourseId(),
							offlineTest.getComponentSubComponentId(),
							offlineTestsFromCsvFile);
					DateTime lastUpdateFromOnline = getLastUpdateFromTestsList(
							onlineTest.getUserId(), onlineTest.getCourseId(),
							onlineTest.getComponentSubComponentId(),
							onlineTestsFromCsvFile);

					if (lastUpdateFromOffline.isAfter(lastUpdateFromOnline)) {
						// offline last update is latest
						testResultService.assertEquals(
								true,
								offlineTest.getLastUpdate().equals(
										onlineTest.getLastUpdate())
										&& offlineTest.getLastUpdate().equals(
												lastUpdateFromOffline
														.toString()));
					}

					// 4.1.4 recalculate average

					double offlineAverage = getAverageFromList(
							offlineTest.getUserId(), offlineTest.getCourseId(),
							offlineTest.getComponentSubComponentId(),
							offlineTestsFromCsvFile);
					double onlineAverage=getAverageFromList(
							onlineTest.getUserId(), onlineTest.getCourseId(),
							onlineTest.getComponentSubComponentId(),
							onlineTestsFromCsvFile);
					
					double expectedAverage=(offlineAverage+onlineAverage)/2;
					
					double offlineDbTestAvrage=Double.parseDouble(offlineTest.getAvarage());
					double onlineDbTestAvrage=Double.parseDouble(onlineTest.getAvarage());
					
					testResultService.assertEquals(true,expectedAverage==onlineDbTestAvrage);

					// double expectedAverage=

					// 4.1.5 check that Times taken is accumulated
					int offlineTimesTaken = getTimesTakenFromList(
							offlineTest.getUserId(), offlineTest.getCourseId(),
							offlineTest.getComponentSubComponentId(),
							offlineTestsFromCsvFile);

					int onlineTimesTaken = getTimesTakenFromList(
							offlineTest.getUserId(), offlineTest.getCourseId(),
							offlineTest.getComponentSubComponentId(),
							onlineTestsFromCsvFile);
					int expectedTimesTaken = offlineTimesTaken
							+ onlineTimesTaken;

					testResultService
							.assertEquals(true, offlineTest.getTimesTaken()
									.equals(onlineTest.getTimesTaken()));
					testResultService.assertEquals(
							true,
							offlineTest.getTimesTaken().equals(
									String.valueOf(expectedTimesTaken)));

					break innerloop2;

				} else {
					System.out.println("Test not found"
							+ offlineTest.getStringArr());
				}
			}

		}

	}

	public void checkProgress(String studentID, String courseId, String ItemId,
			String lastUpdate, int last) {

	}

	public String GetGradeFromList(String studentId, String courseId,
			String compId, List<StudentTest> list) {
		String grade = null;
		for (int i = 0; i < list.size(); i++) {
			if (courseId.equals(list.get(i).getCourseId())
					&& compId.equals(list.get(i).getComponentSubComponentId())) {
				grade = list.get(i).getGrade();
				break;
			}
		}
		return grade;
	}

	public DateTime getLastUpdateFromProgressList(String studentId,
			String courseId, String itemId, List<StudentProgress> list) {
		DateTime dateTime = null;
		for (int i = 0; i < list.size(); i++) {
			if (courseId.equals(list.get(i).getCourseId())
					&& itemId.equals(list.get(i).getItemId())) {

				DateTimeFormatter formatter = DateTimeFormat
						.forPattern("yyyy-MM-dd HH:mm:ss.S");
				DateTime dt = (list.get(i).getLastUpdate());
				dateTime = dt;
				break;
			}
		}
		return dateTime;
	}

	public DateTime getLastUpdateFromTestsList(String studentId,
			String courseId, String compId, List<StudentTest> list) {
		DateTime dateTime = null;
		for (int i = 0; i < list.size(); i++) {
			if (courseId.equals(list.get(i).getCourseId())
					&& compId.equals(list.get(i).getComponentSubComponentId())) {

				DateTimeFormatter formatter = DateTimeFormat
						.forPattern("yyyy-MM-dd HH:mm:ss.S");
				DateTime dt = list.get(i).getLastUpdate();
				dateTime = dt;
				break;
			}
		}
		return dateTime;
	}

	public double getAverageFromList(String studentId, String courseId,
			String compId, List<StudentTest> list) {
		
		double average=0;
		for (int i = 0; i < list.size(); i++) {
			if (courseId.equals(list.get(i).getCourseId())
					&& compId.equals(list.get(i).getComponentSubComponentId())) {
				 average = Double.valueOf(list.get(i).getAvarage());
				break;
			}
		}
		return average;
	}

	public int getTimesTakenFromList(String studentId, String courseId,
			String compId, List<StudentTest> list) {
		int timesTaken = 0;
		for (int i = 0; i < list.size(); i++) {
			if (courseId.equals(list.get(i).getCourseId())
					&& compId.equals(list.get(i).getComponentSubComponentId())) {

				timesTaken = Integer.valueOf(list.get(i).getTimesTaken());
				break;
			}
		}
		return timesTaken;
	}

	public void getDBsDataBeforeSync() throws Exception {

		System.out
				.println("Getting data from Db's before the test and saving them in CSV files");

		offlineTestsFromDb = studentService.getMultipleStudentsTests(
				offlineStudents, InstallationType.Offline);

		onlineTestsFromDb = studentService.getMultipleStudentsTests(
				onlineStudents, InstallationType.Online);

		offlineProgressFromDb = studentService.getMultipleStudentsProgress(
				offlineStudents, InstallationType.Offline);

		onlineProgressFromDb = studentService.getMultipleStudentsProgress(
				onlineStudents, InstallationType.Online);

		textService.writeArrayistToCSVFile(offlineTestCsvPathBeofreSync,
				studentService
						.getStudentTestListIntoArrayList(offlineTestsFromDb));

		textService.writeArrayistToCSVFile(onlineTestCsvPathBeforeSync,
				studentService
						.getStudentTestListIntoArrayList(onlineTestsFromDb));

		textService
				.writeArrayistToCSVFile(
						offlineProgressCsvPathBeforeSync,
						studentService
								.getStudentProgressListIntoArrayList(offlineProgressFromDb));

		textService
				.writeArrayistToCSVFile(
						onlineProgressCsvPathBeforeSync,
						studentService
								.getStudentProgressListIntoArrayList(onlineProgressFromDb));

	}

	@Test
	public void unitTest() throws Exception {
		getDBsDataBeforeSync();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();

	}

}
