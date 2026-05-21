package org.conical.common.bbl.db;

import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

/**
 * Type of database applications may need to read/write to.
 * 
 * @author rdoherty
 */
public enum DatabaseType {
	ORACLE    (true,  "oracle.jdbc.driver.OracleDriver", "oracle.jdbc.pool.OracleDataSource",             "jdbc:oracle:thin:@{0}:{1}:{2}"),
	MYSQL     (true,  "com.mysql.jdbc.Driver",           "com.mysql.jdbc.jdbc2.optional.MysqlDataSource", "jdbc:mysql://{0}:{1}/{2}"),
	POSTGRES  (false, "org.postgresql.Driver",           "edu.upenn.bbl.common.db.PostgresDataSource",    "jdbc:postgresql://{0}:{1}/{2}"),
	FILEMAKER (false, null, null, null),
	SAS_DUMP  (false, null, null, null),
	CUSTOM    (false, null, null, null);

	private boolean _queryable;
	private String _dbDriverClass;
	private String _dataSourceClass;
	private String _connectionPattern;
	
	private DatabaseType(boolean queryable, String dbDriverClass, String dataSourceClass, String connectionPattern) {
		_queryable = queryable;
		_dbDriverClass = dbDriverClass;
		_dataSourceClass = dataSourceClass;
		_connectionPattern = connectionPattern;
	}
	
	/**
	 * @return true if this type of database is queryable using the BBL
	 * common querier libraries (typically only if there exists a JDBC
	 * driver for that type)
	 */
	public boolean isQueryable() {
		return _queryable;
	}

	/**
	 * @return name of the driver class to use to access this DB type
	 */
	public String getDbDriverClass() {
		return _dbDriverClass;
	}
	
	/**
	 * @return name of the DataSource class to use to access this DB type
	 */
	public String getDataSourceClass() {
		return _dataSourceClass;
	}
	
	/**
	 * Formats the given connection information into a URL recognized
	 * by this type of database.
	 * 
	 * @param server server hosting the database
	 * @param port port the database is serving on
	 * @param sid SID, or name of the database
	 * @return connection URL for this type of database
	 */
	public String getConnectionURL(String server, int port, String sid) {
		return MessageFormat.format(_connectionPattern, server, String.valueOf(port), sid);
	}
	
	/**
	 * Parses the passed URL and, if successful, returns an array of Strings
	 * containing [ server, port, database ].
	 * 
	 * @param url connection URL
	 * @return array of strings representing [ server, port, database ]
	 */
	public String[] getUrlParts(String url) throws ParseException {
		Object[] args = new MessageFormat(_connectionPattern).parse(url);
		return new String[]{ (String)args[0], (String)args[1], (String)args[2] };
	}
	
	/**
	 * @param value string value
	 * @return true if this can be converted to a DatabaseType using valueOf(), else false
	 */
	public static boolean isDatabaseType(String value) {
	  try {
	    DatabaseType.valueOf(value);
	    return true;
	  }
	  catch (IllegalArgumentException e) {
	    return false;
	  }
	}

	/**
	 * @return a string containing a human-readable list of the values of this enum
	 */
	public static String getValuesAsString() {
		return "{ " + StringUtils.join(DatabaseType.values(), ", ") + " }";
	}
}
