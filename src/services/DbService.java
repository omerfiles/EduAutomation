package services;

import static org.junit.Assert.fail;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.system.SystemObjectImpl;

import org.junit.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.thoughtworks.selenium.webdriven.commands.RunScript;

import Enums.UserType;
import Objects.Institution;
import Objects.UserObject;

@Service("DbService")
public class DbService extends GenericService {

	private static final String SQL_SERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private JdbcTemplate jdbcTemplate;
	// private final String db_connect_string =
	// "jdbc:sqlserver://BACKQA:1433;databaseName=EDODOTNet3;";
	private String db_userid = null;
	private String db_password = null;
	private final int MAX_DB_TIMEOUT = 120;

	private boolean useOfflineDB;

	// private DataSource dataSou rce;

	public boolean isUseOfflineDB() {
		return useOfflineDB;
	}

	public void setUseOfflineDB(boolean useOfflineDB) {
		this.useOfflineDB = useOfflineDB;
	}

	@Autowired
	Configuration configuration;

	// private String db_connect_string = configuration
	// .getProperty("db.connection");

	// private String db_connect_string = null;

	@Autowired
	InstitutionService institutionService;

	@Autowired
	services.Reporter report;

	Connection conn;

	// private static final Logger logger = Logger.getLogger(DbService.class);

	public DbService() throws Exception {
		jdbcTemplate = new JdbcTemplate();
	}

	public String sig(int size) throws Exception {
		String str = sig();
		return str.substring(str.length() - size, str.length());
	}

	public String sig() throws Exception {
		return String.valueOf(System.currentTimeMillis());
	}

	public String getStringFromQuery(String sql) throws Exception {
		return getStringFromQuery(sql, 10, false);
	}

	public void runDeleteUpdateSql(String sql) throws Exception {
		report.report("Query is: " + sql);
		// db_userid = configuration.getProperty("db.connection.username");
		// db_password = configuration.getProperty("db.connection.password");

		System.out.println(sql);
		Statement statement = null;
		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnection();
			System.out.println("connected");

			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}

			statement = conn.createStatement();
			statement.executeUpdate(sql);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

	}

	public List<String[]> getStringListFromQuery(String sql, int intervals,
			int columns) throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		report.report("Query is: " + sql + ". Max db time out is: "
				+ MAX_DB_TIMEOUT);
		// db_userid = configuration.getProperty("db.connection.username");
		// db_password = configuration.getProperty("db.connection.password");
		// System.out.println(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnection();
			// System.out.println("connected");
			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}
			statement = conn.createStatement();

			rs = statement.executeQuery(sql);

			while (rs.next()) {

				String[] strArr = new String[columns];
				for (int i = 1; i <= columns; i++) {
					strArr[i - 1] = rs.getString(i);
				}
				list.add(strArr);
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			}
			if (statement != null) {
				statement.close();
			}
		}
		return list;
	}

	public List<String[]> getListFromPrepairedStmt(String sql, int columns)
			throws SQLException {
		// System.out.println(sql);
		conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate(); // do not use execute() here otherwise you may get
							// the error
							// The statement must be executed before
							// any results can be obtained on the next
							// getGeneratedKeys statement.
		List<String[]> list = new ArrayList<String[]>();
		ResultSet rs = ps.getGeneratedKeys();
		while (rs.next()) {
			String[] str = new String[columns];
			for (int i = 0; i < columns; i++) {
				str[i] = rs.getString(i + 1);
			}
			list.add(str);
		}

		return list;

	}

	public List<String> getArrayListFromQuery(String sql, int intervals)
			throws Exception {
		List<String> list = new ArrayList<String>();
		report.report("Query is: " + sql + ". Max db time out is: "
				+ MAX_DB_TIMEOUT);
		// db_userid = configuration.getProperty("db.connection.username");
		// db_password = configuration.getProperty("db.connection.password");
		System.out.println(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = getConnection();
			System.out.println("connected");
			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}
			statement = conn.createStatement();

			rs = statement.executeQuery(sql);
			while (rs.next()) {
				// System.out.println(rs.getString(1));
				// str = rs.getString(1);
				list.add(rs.getString(1));
				System.out.println(rs.getString(1));
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

			} catch (Exception e) {
			}
			if (statement != null) {
				statement.close();
			}
		}
		return list;
	}

	public String getStringFromQuery(String sql, int intervals,
			boolean allowNull) throws Exception {
		// System.out.println(configuration.getProperty("db.connection"));
		report.report("Query is: " + sql + ". Max db time out is: "
				+ MAX_DB_TIMEOUT);
		// db_userid = configuration.getProperty("db.connection.username");
		// db_password = configuration.getProperty("db.connection.password");
		System.out.println(db_userid + "  " + db_password);
		System.out.println(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			System.out.println("DB user id is: " + db_userid);
			conn = getConnection();
			System.out.println("connected");

			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}

			statement = conn.createStatement();
			outerloop: while (elapsedTimeInSec < MAX_DB_TIMEOUT) {
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					// System.out.println(rs.getString(1));
					str = rs.getString(1);

				}
				if (str != null) {
					report.report("DB result found");

					break outerloop;

				} else {
					Thread.sleep(intervals * 1000);
					report.report("Waiting for DB. sleeping for " + intervals
							+ " seconds");
					elapsedTimeInSec = elapsedTimeInSec + intervals;
				}
			}

			conn.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (str == null && allowNull == false) {
					Assert.fail("Query result is null. Query was: " + sql);
				} else if (str == null && allowNull == true) {
					return null;
				}

			} catch (Exception e) {

			}

			if (statement != null) {
				statement.close();
			}

		}
		return str;
	}

	public SQLXML getSQLXMLFromQuery(String sql) throws Exception {
		// System.out.println(configuration.getProperty("db.connection"));
		ResultSet rs = null;
		Statement statement = null;
		SQLXML str = null;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);

			conn = getConnection();
			System.out.println("connected");

			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}
			statement = conn.createStatement();

			rs = statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1));
				str = rs.getSQLXML(1);

			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return str;
	}

	public void closeConnection() throws SQLException {
		conn.close();
	}

	public String getUserIdByUserName(String userName, String institutionId)
			throws Exception {
		// String institutionid =
		// institutionService.getInstitution().getInstitutionId();
		String sql = "select UserId from users where UserName='" + userName
				+ "' and institutionid=" + institutionId;
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getUserNameById(String id, String institutionId)
			throws Exception {
		// String institutionid =
		// institutionService.getInstitution().getInstitutionId();
		String sql = "select UserName from users where userId='" + id
				+ "' and institutionid=" + institutionId;
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getInstituteNameById(String id) throws Exception {
		String sql = "select name from institutions where institutionId=" + id;
		String result = getStringFromQuery(sql);
		return result;
	}

	public String getInstituteIdByName(String name) throws Exception {
		String sql = "select institutionId from institutions where name='"
				+ name + "'";
		String result = getStringFromQuery(sql);
		return result;
	}

	public void verifyInstitutionCreated(Institution institution)
			throws Exception {
		String sql = "select institutionId from institutions where Name='"
				+ institution.getName() + "' and cannonicalDomain='"
				+ institution.getHost() + "'";
		String result = getStringFromQuery(sql);
		report.report("Institution id is: " + result);
	}

	public String getClassIdByName(String classNae, String institutionId)
			throws Exception {

		// String institutionId =
		// institutionService.getInstitution().getInstitutionId();

		String sql = "select classId from class where Name='" + classNae
				+ "' and institutionId=" + institutionId + "";
		String result = getStringFromQuery(sql);
		return result;
	}

	public int getCurrentDay() throws Exception {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		String dayOfMonthStr = String.valueOf(dayOfMonth);

		return dayOfMonth;
	}

	public String getDbConnString() {
		return configuration.getProperty("db.connection");

	}

	public String getOfflineConnString() {
		String conString = configuration.getGlobalProperties("offlinedb");
		// conString = conString.replace("%machine%",
		// configuration.getGlobalProperties("offline.ip"));
		return conString;
	}

	public int getUsedLicensesPerClass(String className) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] getClassAndCourseWithLastProgress(String teacherName,
			String instId, UserType userType) throws Exception {

		String[] output = new String[2];

		String sql = "select Top(1) userId,CourseId from Progress where UserId in( select distinct  UserId from ClassUsers ";

		if (userType.equals(UserType.Teahcer)) {
			String sqlAsTeacher = " where ClassId in(select ClassId from dbo.TeacherClasses where UserPermissionsId=(select up.UserPermissionsId from dbo.UserPermissions as up, users as u where  up.userId=u.userId and u.userName='"
					+ teacherName + "' and u.institutionId=" + instId + ") ))";
			sql = sql + sqlAsTeacher;
		} else if (userType.equals(UserType.SchoolAdmin)) {
			String asSchoolAdmin = "where ClassId in( select ClassId  from class where institutionId="
					+ instId + "))";
			sql = sql + asSchoolAdmin;
		}

		sql = sql + "			order by LastUpdate desc";

		List<String[]> results = getStringListFromQuery(sql, 5, 2);

		String StudentId = results.get(0)[0];
		String sqlForClassName = "select c.name from classUsers as cu , class as c where cu.ClassId=c.ClassId  and cu.userId="
				+ StudentId;
		output[0] = getStringFromQuery(sqlForClassName);

		String sqlForCourseName = "select Name from course where CourseId="
				+ results.get(0)[1];

		output[1] = getStringFromQuery(sqlForCourseName);

		return output;

	}

	public String getNumberOfStudentsInClass(String className,
			String institutionId) throws Exception {
		String sql = "  select count(*) from classUsers as cu,class as c where cu.classId=c.classId and c.name='"
				+ className + "' and c.InstitutionId=" + institutionId;
		String result = getStringFromQuery(sql);
		return result;
	}

	public List<List> getListFromStoreRrecedure(String sql) throws SQLException {
		List<List> rsList = new ArrayList<List>();
		System.out.println("SQL query was: " + sql);
		try {
			conn = getConnection();

			System.out.println("connected");
			if (conn.isClosed() == true) {
				System.out.println("connection is closed");
			}
			// statement = conn.createStatement();

			CallableStatement statement = conn.prepareCall(sql);
			// for (int i = 0; i < params.length; i++) {
			// statement.setString(i, params[i]);
			// }

			boolean results = statement.execute();

			int rsCount = 0;
			while (results) {
				List<String[]> strList = new ArrayList<String[]>();
				ResultSet rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				int columnsNumber = rsmd.getColumnCount();

				while (rs.next()) {
					String[] strArr = new String[columnsNumber];
					for (int i = 0; i < columnsNumber; i++) {
						strArr[i] = rs.getString(i + 1);
					}
					strList.add(strArr);
				}

				rs.close();
				rsList.add(strList);
				results = statement.getMoreResults();
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsList;
	}

	// s}

	public String getCourseIdByName(String courseName) throws Exception {
		String sql = "select CourseId from course where name='" + courseName
				+ "'";
		return getStringFromQuery(sql);

	}

	public List<String[]> getClassScores(String classId, String courseId)
			throws Exception {
		String sql = "select  avg( tr.Grade) from testResults as tr  where tr.userId in( select distinct  UserId from ClassUsers where ClassId="
				+ classId
				+ " and courseId="
				+ courseId
				+ " )group by ComponentSubComponentId";

		List<String[]> results = getStringListFromQuery(sql, 1, 1);
		return results;
	}

	public int getRandonNumber(int min, int max) {
		Random r = new Random();
		int i1 = r.nextInt(max - min + 1) + min;
		return i1;
	}

	public Connection getConnection() throws SQLException {

		if (isUseOfflineDB()) {
			db_userid = configuration.getGlobalProperties("offline.user");
			db_password = configuration.getGlobalProperties("offline.pass");
			conn = DriverManager.getConnection(getOfflineConnString(),
					db_userid, db_password);
		} else {
			db_userid = configuration.getProperty("db.connection.username");
			db_password = configuration.getProperty("db.connection.password");
			conn = DriverManager.getConnection(getDbConnString(), db_userid,
					db_password);
		}

		return conn;
	}

	// public void runStorePrecedure(String precedureName, String... params)
	// throws SQLException {
	// String SPsql = "EXEC " + precedureName + " ?,?,?"; // for stored proc
	// // taking 2
	// // parameters
	// conn = getConnection();
	// CallableStatement statement = conn.prepareCall(SPsql);
	// // statement.registerOutParameter(0, params[0]);
	// }

	public void runStorePrecedure(String sp) throws SQLException {
		runStorePrecedure(sp, false, false);
	}

	public void runStorePrecedure(String sp, boolean executeQuery)
			throws SQLException {
		runStorePrecedure(sp, executeQuery, false);
	}

	public void runStorePrecedure(String sp, boolean executeQuery,
			boolean executeBatch) throws SQLException {
		try {
			conn = getConnection();
			CallableStatement statement = conn.prepareCall(sp);
			System.out.println("SP was: " + sp);
			if (executeBatch == false) {
				if (executeQuery == true) {
					// for offline DB
					statement.executeQuery();
				} else {
					statement.executeUpdate();
				}
			} else {
				PreparedStatement ps = conn.prepareStatement(sp);
				ps.execute();
				conn.commit();
				ps.clearBatch();
			}
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getUnitNamesByCourse(String courseId) throws Exception {
		String sql = "	select UnitName from Units where CourseId=" + courseId
				+ " order by Sequence";

		return getArrayListFromQuery(sql, 1);
	}

	public void setInstitutionLastOfflineSyncToNull(String institutionId)
			throws Exception {
		String sql = "update Institutions set LastOfflineSync =null where InstitutionId="
				+ institutionId;
		runDeleteUpdateSql(sql);

	}
}
