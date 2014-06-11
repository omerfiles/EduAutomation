package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;

import javax.sql.DataSource;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.system.SystemObjectImpl;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import Objects.MailMessage;
import static org.junit.Assert.fail;

@Service
public class DbService extends SystemObjectImpl {

	private static final String SQL_SERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private JdbcTemplate jdbcTemplate;
	private final String db_connect_string = "jdbc:sqlserver://BACKQA:1433;databaseName=EDODOTNet3;integratedSecurity=true;";
	private final String db_userid = "EDUSOFT2k\\omers";
	private final String db_password = "Shu111";
	private final int MAX_DB_TIMEOUT = 120;
	// private DataSource dataSource;

	@Autowired
	Configuration configuration;

	Connection conn;

	private static final Logger logger = Logger.getLogger(DbService.class);

	public DbService() throws Exception {
		jdbcTemplate = new JdbcTemplate();
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		// this.dataSource = dataSource;
	}

	public void runUpdateQuery(String query) throws Exception {
		try {
			report.startLevel("Running update query: " + query,
					EnumReportLevel.CurrentPlace);
			jdbcTemplate.update(query);
		} catch (Exception e) {
			fail("Update query failed");
		}
		report.stopLevel();

	}

	public String getValue(String query, String columnName) throws Exception {
		String str = null;
		report.startLevel("Getting results set from db for query: " + query,
				EnumReportLevel.CurrentPlace);
		report.report("Query was:" + query);
		// String result = (String) jdbcTemplate.queryForObject(query,
		// String.class);
		Statement statement = conn.createStatement();
		String queryString = "select * from class where classId=500020001";
		ResultSet rs = statement.executeQuery(queryString);
		str = rs.getString(0);

		return str;
	}

	public void deleteValue(String tableName, String whereParam,
			String whereValue) throws Exception {
		String sql = "";
		try {
			sql = "delete from ";
			sql += tableName;
			sql += " where " + whereParam + " =" + whereValue + "";
			report.report("Query to run:" + sql);
			jdbcTemplate.update(sql);
		} catch (Exception e) {
			report.report(e.getMessage());
			Assert.fail("Sql delete failed. Query was:" + sql);
		}
	}

	public SqlRowSet getValueRS(String query, String[] columnName,
			int NumOfRowsExpected, int timeout) throws Exception {
		report.startLevel("Getting results set from db",
				EnumReportLevel.CurrentPlace);
		report.report("Query was:" + query);
		SqlRowSet sqlRowSet = null;
		// ResultSet rs=null;
		int elapsedTime = 0;
		while (elapsedTime < timeout) {
			sqlRowSet = jdbcTemplate.queryForRowSet(query);
			report.report("SqlRowSet size is: " + sqlRowSet.getRow());
			if (sqlRowSet.getRow() < 2) {
				Thread.sleep(1000);
				report.report("Sleeping for 1000ms. Timeout is: " + timeout);
				elapsedTime++;
				if (elapsedTime == timeout) {
					Assert.fail("No records found");
				}
			} else
				break;

		}

		String[] result = new String[columnName.length];
		StringBuilder printResults = new StringBuilder("|| ");

		// while (sqlRowSet.next()) {
		// for (int i = 0; i < result.length; i++) {
		// result[i] = sqlRowSet.getString(columnName[i]);
		// sendResults.append(result[i]).append(" ||| ");
		// }
		// }
		report.report("Query results are: " + printResults.toString());
		return sqlRowSet;

	}

	public String[] getValueMultipleColumns(String query, String[] columnName)
			throws Exception {
		String[] results = null;
		int elapsedTime = 0;
		int timeout = 20;

		report.startLevel("Getting results set from db",
				EnumReportLevel.CurrentPlace);
		report.report("Query was:" + query);

		System.out.println("jdbcTemplate = " + jdbcTemplate);
		SqlRowSet sqlRowSet = null;
		outterloop: while (elapsedTime < timeout) {
			try {
				sqlRowSet = jdbcTemplate.queryForRowSet(query);
				while (sqlRowSet.next()) {
					for (int i = 0; i < columnName.length; i++) {
						report.report(columnName[i] + ":"
								+ sqlRowSet.getString(columnName[i]));
					}
					if (sqlRowSet.getString(columnName[1]) != null) {
						elapsedTime = timeout;
						break outterloop;
					} else {

					}
				}
				report.report("Sleeping for 1000ms");
				Thread.sleep(1000);
				elapsedTime++;
			} catch (Exception e) {
				report.report("Sleeping for 1000ms");
				Thread.sleep(1000);
				elapsedTime++;
				continue outterloop;
			}
		}

		try {
			// sqlRowSet = jdbcTemplate.queryForRowSet(query);
			// int rowSetSize = getSqlRowSetRowsCount(sqlRowSet);
			results = new String[columnName.length];
			StringBuilder printResults = new StringBuilder("|| ");
			sqlRowSet.first();
			for (int i = 0; i < columnName.length; i++) {
				results[i] = sqlRowSet.getString(columnName[i]);
				printResults.append(results[i]).append(" ||| ");
			}
		} catch (Exception e) {

		}

		return results;
	}

	public String[] getValue(String query, String[] columnName,
			int NumOfRowsExpected, int timeout) throws Exception {
		report.startLevel("Getting results set from db",
				EnumReportLevel.CurrentPlace);
		report.report("Query was:" + query);
		System.out.println("query = " + query);
		System.out.println("jdbcTemplate = " + jdbcTemplate);
		SqlRowSet sqlRowSet = null;
		int elapsedTime = 0;
		while (elapsedTime < timeout) {
			// sqlRowSet = jdbcTemplate.queryForRowSet(query);
			// if(sqlRowSet.getString(columnName[0])==null)
			// {
			// Thread.sleep(1000);
			// report.report("Sleeping for 1000ms");
			// elapsedTime++;
			// }
			// else
			// break;

			try {
				sqlRowSet = jdbcTemplate.queryForRowSet(query);
				while (sqlRowSet.next()) {
					report.report(sqlRowSet.getString(columnName[0]));
					if (sqlRowSet.getString(columnName[0]) != null) {
						elapsedTime = timeout;
						break;
					} else {

					}
				}
				report.report("Sleeping for 1000ms");
				Thread.sleep(1000);
				elapsedTime++;
			} catch (Exception e) {
				report.report("Sleeping for 1000ms");
				Thread.sleep(1000);
				elapsedTime++;
				continue;
			}

		}
		if (getSqlRowSetRowsCount(sqlRowSet) > NumOfRowsExpected
				&& NumOfRowsExpected != 0) {
			Assert.fail("Too many rows returned in Quwey");
		}
		int rowSetSize = getSqlRowSetRowsCount(sqlRowSet);
		String[] result = new String[rowSetSize];
		StringBuilder printResults = new StringBuilder("|| ");
		sqlRowSet.first();
		// int i=0;
		// while (sqlRowSet.next()) {
		if (result.length > 0) {
			for (int i = 0; i < rowSetSize; i++) {

				result[i] = sqlRowSet.getString(columnName[0]);
				printResults.append(result[i]).append(" ||| ");
				sqlRowSet.next();
				// i++;
				// }
				// }
			}
		}

		report.report("Query results are: " + printResults.toString());
		return result;

	}

	public String[] getValue(String query, String[] columnName,
			int NumOfRowsExpected) throws Exception {

		return getValue(query, columnName, NumOfRowsExpected, 20);

	}

	public SqlRowSet getValuesRs(String[] valuesToReturn, String tableName,
			String[] whereParam, String[] whereValue, int numOfRowsExpected)
			throws Exception {
		String query = "select ";
		try {
			report.startLevel("Starting to build sql query",
					Reporter.EnumReportLevel.CurrentPlace);
			if (whereParam.length != whereValue.length
					|| whereParam.length == 0) {
				fail("Query params are missing or incorrect");
			}

			if (valuesToReturn.length == 1) {
				query = query + valuesToReturn[0];
			} else {
				for (int i = 0; i < valuesToReturn.length; i++) {
					query = query + valuesToReturn[i];
					if (i < valuesToReturn.length - 1) {
						query = query + ",";
					}
				}
			}
			report.report("Query string: " + query);
			query = query + " from " + tableName + " where ";

			query = query + whereParam[0] + "='" + whereValue[0] + "'";
			if (whereParam.length > 1) {
				for (int i = 1; i < whereParam.length; i++) {
					query = query + " and ";
					query = query + whereParam[i] + "='" + whereValue[i] + "'";
				}
			}
			report.report(query);
			report.stopLevel();
			return getValueRS(query, valuesToReturn, numOfRowsExpected, 20);
		} catch (Exception e) {
			fail("Select query failed. Query was :" + query + ". Stack trace: "
					+ e.getMessage());

			e.printStackTrace();
			throw e;
		}
	}

	public String[] getValues(String[] valuesToReturn, String tableName,
			String[] whereParam, String[] whereValue, int numOfRowsExpected)
			throws Exception {
		String query = "select ";
		try {
			report.startLevel("Starting to build sql query",
					Reporter.EnumReportLevel.CurrentPlace);
			if (whereParam.length != whereValue.length
					|| whereParam.length == 0) {
				fail("Query params are missing or incorrect");
			}

			if (valuesToReturn.length == 1) {
				query = query + valuesToReturn[0];
			} else {
				for (int i = 0; i < valuesToReturn.length; i++) {
					query = query + valuesToReturn[i];
					if (i < valuesToReturn.length - 1) {
						query = query + ",";
					}
				}
			}
			report.report("Query string: " + query);
			query = query + " from " + tableName + " where ";

			query = query + whereParam[0] + "='" + whereValue[0] + "'";
			if (whereParam.length > 1) {
				for (int i = 1; i < whereParam.length; i++) {
					query = query + " and ";
					query = query + whereParam[i] + "='" + whereValue[i] + "'";
				}
			}
			report.report(query);
			report.stopLevel();
			return getValue(query, valuesToReturn, numOfRowsExpected);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Select query failed. Query was :" + query + ". Stack trace: "
					+ e.getMessage());
			throw e;
		}
	}

	public String sig() throws Exception {
		return String.valueOf(System.currentTimeMillis());
	}

	public int getSqlRowSetRowsCount(SqlRowSet rs) throws Exception {
		rs.first();
		int count = 0;

		try {
			if (rs.getString(1) != null) {
				count++;
			}

			while (rs.next()) {
				count++;
				// report.report("Number of rows is: " + count);
			}
		} catch (Exception e) {
			report.report("zero rows found");

		} finally {
			return count;
		}
	}

	public String getStringFromQuery(String sql) throws Exception {
		return getStringFromQuery(sql, 10);
	}

	public void runDeleteUpdateSql(String sql) throws Exception {
		report.report("Query is: " + sql);
		System.out.println(sql);
		Statement statement = null;
		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);
			conn = DriverManager.getConnection(db_connect_string, db_userid,
					db_password);
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

	public String getStringFromQuery(String sql, int intervals)
			throws Exception {
		// System.out.println(configuration.getProperty("db.connection"));
		report.report("Query is: " + sql);
		System.out.println(sql);
		ResultSet rs = null;
		Statement statement = null;
		String str = null;
		int elapsedTimeInSec = 0;

		try {
			Class.forName(SQL_SERVER_DRIVER_CLASS);

			conn = DriverManager.getConnection(db_connect_string, db_userid,
					db_password);
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

			conn = DriverManager.getConnection(db_connect_string, db_userid,
					db_password);
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

	public String getUserIdByUserName(String userName) throws Exception {
		String institutionid=configuration.getProperty("institution.id");
		String sql = "select UserId from users where UserName='" + userName
				+ "' and institutionid="+institutionid;
		String result = getStringFromQuery(sql);
		return result;
	}

	public void setUserLoginToNull(String id) {
		String sql="Update users set logedin = null where userid="+id;
	}

}
