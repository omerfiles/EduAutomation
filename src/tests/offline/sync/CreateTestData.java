package tests.offline.sync;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.StringUtils;

import Enums.InstallationType;
import tests.misc.EdusoftBasicTest;

public class CreateTestData extends EdusoftBasicTest {

	@Test
	public void setSubmitTestForOffline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOffline//input//submitTest.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOffline//output/submit.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);

		System.out.println("Iterate on csv file data");
		String componentId;

		studentId = list.get(0)[15].toString();

		String tempCompId = "";
		String sqlText = "exec SubmitTest @UserId=%userId%"
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks=%marks%,@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";
		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {

			componentId = list.get(i)[12].toString();

			if (!tempCompId.equals(componentId)) {
				// new component
				try {
					tempCompId = componentId;

					System.out
							.println("Create the query and add it to sqlTextForFile");
					sql = sqlText;
					sql = sql.replace("%userId%", list.get(i)[15].toString());
					sql = sql.replace("%unitId%", list.get(i)[11].toString());
					sql = sql.replace("%compId%", componentId);
					sql = sql.replace("%marks%", getStringFromMarks(marks));
					sql = sql.replace("%grade%",
							String.valueOf(calcGrade(marks)));

					sqlTextForFile.add(sql);
					System.out.println(sql);
					marks.clear();
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					System.out
							.println("A field may be missing from the CSV file");
					testResultService
							.addFailTest("A field may be missing from the CSV file");
				}

			} else {
				// same component
				// grades.add(list.get(i)[15].toString());
				marks.add(list.get(i)[14].toString());
			}
			tempCompId = componentId;

			// textService.writeListToCsvFile(sqlFilePath, sqlTextForFile);

		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	@Test
	public void setSubmitTestForOnline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOnline//input//submitTest.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOnline//output/submit.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);

		System.out.println("Iterate on csv file data");
		String componentId;

		studentId = list.get(0)[15].toString();

		String tempCompId = "";
		String sqlText = "exec SubmitTest @UserId=%userId%"
				+ ",@UnitId=%unitId%,@ComponentId=%compId%,@Grade=%grade%,@Marks=%marks%,@SetId='23277|23278|23279|23280|23281|',@VisitedItems='[1][2][3][4][5]',@TimeOver=0,@UserState=0x7B2261223A5B7B2269436F6465223A22623372706C6F74303031222C22694964223A32333237372C22695479706,@TestTime=3200";
		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {

			componentId = list.get(i)[12].toString();

			if (!tempCompId.equals(componentId)) {
				// new component
				tempCompId = componentId;

				System.out
						.println("Create the query and add it to sqlTextForFile");
				sql = sqlText;
				sql = sql.replace("%userId%", list.get(i)[15].toString());
				sql = sql.replace("%unitId%", list.get(i)[11].toString());
				sql = sql.replace("%compId%", componentId);
				sql = sql.replace("%marks%", getStringFromMarks(marks));
				sql = sql.replace("%grade%", String.valueOf(calcGrade(marks)));

				sqlTextForFile.add(sql);
				System.out.println(sql);
				marks.clear();

			} else {
				// same component
				// grades.add(list.get(i)[15].toString());
				marks.add(list.get(i)[14].toString());
			}
			tempCompId = componentId;

			// textService.writeListToCsvFile(sqlFilePath, sqlTextForFile);

		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	@Test
	public void setStudentProgressForOffline() throws Exception {
		// String csvFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\input\\setProgress.csv";

		String csvFilePath = "smb://storage//QA//Automation//testData//forOffline//input//setProgress.csv";

		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOffline//output/setProgress.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);
		studentId = list.get(0)[14];
		System.out.println("Iterate on csv file data");
		String componentId;
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
				+ studentId
				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";

		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {
			sql = sqlText;
			sql = sql.replace("%courseId%", list.get(i)[10]);
			sql = sql.replace("%itemId%", list.get(i)[13]);
			sqlTextForFile.add(sql);
			System.out.println(sql);
		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	@Test
	public void setStudentProgressForOnline() throws Exception {
		String csvFilePath = "smb://storage//QA//Automation//testData//forOnline//input//setProgress.csv";
		List<String[]> list = new ArrayList<String[]>();
		// String sqlFilePath =
		// "\\\\storage\\QA\\Automation\\testData\\forOffline\\output";
		String sqlFilePath = "smb://storage//QA//Automation//testData//forOnline//output/setProgress.sql";
		String studentId = null;

		List<String> sqlTextForFile = new ArrayList<String>();

		System.out.println("Import data from csv file");
		list = textService.getStr2dimArrFromCsv(csvFilePath, true);
		studentId = list.get(0)[14];
		System.out.println("Iterate on csv file data");
		String componentId;
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId=%userId%"

				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";

		List<String> marks = new ArrayList<String>();
		String sql;
		for (int i = 0; i < list.size(); i++) {
			sql = sqlText;
			sql = sql.replace("%userId%", list.get(i)[14]);
			sql = sql.replace("%courseId%", list.get(i)[10]);
			sql = sql.replace("%itemId%", list.get(i)[13]);
			sqlTextForFile.add(sql);
			System.out.println(sql);
		}
		textService.writeListToSmbFile(sqlFilePath, sqlTextForFile,
				netService.getDomainAuth());

	}

	public String getStringFromMarks(List<String> marks) {
		String result = "'";
		for (int i = 0; i < marks.size(); i++) {
			result += marks.get(i) + "|";
		}
		result += "'";
		return result;
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

	public List<String> createProgressForMultipleStudents(
			InstallationType type, String indstitutionId) throws Exception {
		String[] students = studentService.getInstitutionStudetns(type,
				indstitutionId);
		List<String> spList = new ArrayList<String>();
		for (int i = 0; i < students.length; i++) {
			spList.addAll(createProgressSqlRecordsForStudent(students[i]));
		}
		return spList;
	}

	public List<String> createProgressSqlRecordsForStudent(String studentId)
			throws Exception {
		List<String> spList = new ArrayList<String>();
		List<String[]> coursesDetails = textService
				.getStr2dimArrFromCsv("files/offline/peru/PeruOfflineCoursesUnits.csv");
		String sqlText = "exec SetProgress @CourseId=%courseId%,@ItemId=%itemId%,@UserId="
				+ studentId

				+ ",@Last=1,@Seconds=60,@Visited=1,@ComponentTypeId=1";
		for (int i = 0; i < coursesDetails.size(); i++) {
			String sqlForAdd = sqlText;
			sqlForAdd = sqlForAdd.replace("%courseId%",
					coursesDetails.get(i)[0]);
			sqlForAdd = sqlForAdd.replace("%itemId%", coursesDetails.get(i)[1]);

			spList.add(sqlForAdd);
		}
		return spList;

	}

	@Test
	public void unitTestcreateProgressSqlRecordsForStudent() throws Exception {
		String studentId="655054700082";
		String instId="5231878";
		String []studentIds=studentService.getInstitutionStudetns(InstallationType.Online, instId);
		String filePath="files/offline/peru/"+instId+".sql";
		File file=new File(filePath);
		//clear file
				PrintWriter writer = new PrintWriter(file);
				writer.print("");
				writer.close();
		for(int i=0;i<studentIds.length;i++){
		textService.writeListToCsvFile(filePath,
				createProgressSqlRecordsForStudent(studentIds[i]),true);
		}
		// createProgressForMultipleStudents(InstallationType.Online,
		// "5231874");
	}
}
