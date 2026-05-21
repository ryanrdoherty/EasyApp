package org.conical.common.bbl.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;
import java.util.Date;

import org.conical.common.bbl.exception.BBLRuntimeException;

/**
 * Defines a set of static database utility methods
 * 
 * @author rdoherty
 */
public class DbUtil {

	private static final String SEQUENCE_NAME_WILDCARD = "$SEQUENCE_NAME";
	private static final String NEXT_SEQUENCE_VAL_SQL =
		"select " + SEQUENCE_NAME_WILDCARD + ".nextval from dual";
	
	/**
	 * Quietly closes an array of Wrapper objects.  Currently the following classes
	 * are supported (all other classes, nulls, and already-closed objects are ignored):
	 * <ul>
	 *   <li>java.sql.Connection</li>
	 *   <li>java.sql.Statement</li>
	 *   <li>java.sql.PreparedStatement</li>
	 *   <li>java.sql.ResultSet</li>
	 * </ul>
	 * 
	 * @param wrappers array of wrappers to close
	 */
	public static void close(Wrapper... wrappers) {
		for (Wrapper w : wrappers) {
			if (w != null) {
				try {
					if (w instanceof Connection) {
						((Connection)w).close();
					}
					else if (w instanceof PreparedStatement) {
						((PreparedStatement)w).close();
					}
					else if (w instanceof Statement) {
						((Statement)w).close();
					}
					else if (w instanceof ResultSet) {
						((ResultSet)w).close();
					}
				}
				catch (Exception e) {
					// do nothing; closing quietly
				}
			}
		}
	}

	/**
	 * Returns the next value in the named sequence given the passed connection.  The
	 * SQL generated for this query assumes Oracle as the DB server.
	 * 
	 * @param conn connection to use
	 * @param sequenceName name of the sequence from which to get the nexty value
	 * @return next value in the sequence
	 */
	public static int getNextSequenceValue(Connection conn, String sequenceName) {
		String sql = NEXT_SEQUENCE_VAL_SQL.replace(SEQUENCE_NAME_WILDCARD, sequenceName);
		return executeSingleIntResultQuery(conn, sql,
				"Sequence nextval sql ran without error but produced zero results.",
				"Unable to look up next value in sequence " + sequenceName);
	}
	
	/**
	 * Executes a give SQL query on the given connection and returns a single integer
	 * result.  This method ignores results beyond the first column and beyond the
	 * first returned row.  If no results are returned or a SQL exception occurs
	 * (e.g. bad query, first resulting column is not an integer), an exception will
	 * be thrown with a default message.
	 * 
	 * @param conn connection to use
	 * @param sql sql to execute
	 * @return a single result
	 */
	public static int executeSingleIntResultQuery(Connection conn, String sql) {
		return executeSingleIntResultQuery(conn, sql,
				"No results found for query: " + sql,
				"Unable to execute query " + sql);
	}

	/**
	 * Executes a give SQL query on the given connection and returns a single integer
	 * result.  This method ignores results beyond the first column and beyond the
	 * first returned row.  If no results are returned, an exception will be thrown
	 * containing the text in <code>noResultsErrorMsg</code>.  If a SQL exception
	 * occurs (e.g. bad query, first resulting column is not an integer), an
	 * exception will be thrown containing the text in <code>sqlExceptionErrorMsg</code>.
	 * 
	 * @param conn connection to use
	 * @param sql sql to execute
	 * @param noResultsErrorMsg text in exception if no results are returned
	 * @param sqlExceptionErrorMsg text in exception if SQLException occurs
	 * @return a single result
	 */
	public static int executeSingleIntResultQuery(Connection conn, String sql,
			String noResultsErrorMsg, String sqlExceptionErrorMsg) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt(1);
			}
			// caller screwed up with bad sql
			throw new BBLRuntimeException(noResultsErrorMsg);
		}
		catch (SQLException sqle) {
			throw new BBLRuntimeException(sqlExceptionErrorMsg, sqle);
		}
		finally {
			close(rs, stmt);
		}
	}

	/**
	 * Converts a java.util.Date to a java.sql.Date.  If the date passed
	 * is null, null will be returned.
	 * 
	 * @param date date to convert
	 * @return new date, or null
	 */
	public static java.sql.Date getSqlDate(Date date) {
		return (date == null ? null : new java.sql.Date(date.getTime()));
	}
}
