package org.conical.common.bbl.db;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.conical.common.bbl.exception.ConfigurationRuntimeException;
import org.conical.common.bbl.util.PropertyMapLoader;

/**
 * This class wraps the information needed to establish a connection or connection
 * pool to a database.  Objects of this class are immutable.
 * 
 * @author rdoherty
 */
public class DataSourceConfig {

	private static final boolean DEFAULT_CONN_POOLING_SETTING = true;
	
	public static final String DB_TYPE_KEY = "db.type";
	public static final String DB_CONNECTION_URL_KEY = "db.connection.url";
	public static final String DB_SERVER_NAME_KEY = "db.server.name";
	public static final String DB_SERVER_PORT_KEY = "db.server.port";
	public static final String DB_NAME_KEY = "db.name";
	public static final String DB_LOGIN_NAME_KEY = "db.login.name";
	public static final String DB_LOGIN_PASSWORD_KEY = "db.password";
	public static final String DB_CONNECTION_POOLING_KEY = "db.conn.pool";
	
	private DatabaseType _dbType;
	private String _url;
	private String _server;
	private int _port;
	private String _dbName; // or oracle SID
	private String _user;
	private String _password;
	private boolean _isPooled;
	
	/**
	 * Constructs object using connection URL.  The URL will be checked against the dbType.
	 * 
	 * @param dbType type of database
	 * @param url connection URL
	 * @param user user to connect as
	 * @param password password to verify user
	 * @param isPooled whether this connection should be pooled
	 */
	public DataSourceConfig(DatabaseType dbType, String url, String user, String password, boolean isPooled) {
		init(dbType, url, user, password, isPooled);
	}
	
	private void init(DatabaseType dbType, String url, String user, String password, boolean isPooled) {
		try {
			_dbType = dbType;
			_url = url;
			String[] urlParts = _dbType.getUrlParts(url);
			_server = urlParts[0];
			_port = Integer.parseInt(urlParts[1]);
			_dbName = urlParts[2];
			_user = user;
			_password = password;
			_isPooled = isPooled;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to parse URL: " + url, e);
		}
	}

	/**
	 * Constructs object using server name, port, and database name (instead of URL).  A URL specific
	 * to the database type is constructed using these elements.
	 * 
	 * @param dbType type of database
	 * @param server database server
	 * @param port port DB is served out of
	 * @param dbName name of database (or Oracle SID)
	 * @param user user to connect as
	 * @param password password to verify user
	 * @param isPooled whether this connection should be pooled
	 */
	public DataSourceConfig(DatabaseType dbType, String server, int port, String dbName, String user, String password, boolean isPooled) {
		init( dbType, server, port, dbName, user, password, isPooled);
	}
	
	private void init(DatabaseType dbType, String server, int port, String dbName, String user, String password, boolean isPooled) {
		try {
			_dbType = dbType;
			_server = server;
			_port = port;
			_dbName = dbName;
			_url = _dbType.getConnectionURL(_server, _port, _dbName);
			_user = user;
			_password = password;
			_isPooled = isPooled;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to format URL from parts: " + _server + ", " + _port + ", " + _dbName, e);
		}
	}
	
	/**
	 * Constructs object given a set of parameters in a configuration bundle (i.e.
	 * properties file).  See constants in this class for what values should appear
	 * in the properties file.  If URL is present, server, port and dbName will be
	 * ignored.  Pooling setting is not required; default is to create a connection pool.
	 * 
	 * @param bundleName name of the bundle containing DB connection properties
	 */
	public DataSourceConfig(String bundleName) {
		
		Map<String, String> props = PropertyMapLoader.loadProperties(bundleName);

		// check for required properties and typing
		if (!PropertyMapLoader.requiredPropertiesPresent(props, DB_TYPE_KEY, DB_LOGIN_NAME_KEY, DB_LOGIN_PASSWORD_KEY)) {
			throw new ConfigurationRuntimeException(">0 required database properties (dbtype, login, password) not present.");
		}
		if (!DatabaseType.isDatabaseType(props.get(DB_TYPE_KEY))) {
			throw new ConfigurationRuntimeException("Database type must be one of " + DatabaseType.getValuesAsString()); 
		}
		
		// set (optional) connection pooling setting
		String boolStr = props.get(DB_CONNECTION_POOLING_KEY);
		boolean usePooling = DEFAULT_CONN_POOLING_SETTING;
		if (StringUtils.isEmpty(boolStr)) {
			if (!boolStr.equalsIgnoreCase("true") && !boolStr.equalsIgnoreCase("false")) {
				throw new ConfigurationRuntimeException("Database connection pool setting must be (ignoring case) true or false");
			}
			usePooling = Boolean.valueOf(boolStr);
		}
		
		// check for connection URL
		if (StringUtils.isEmpty(props.get(DB_CONNECTION_URL_KEY))) {
			if (!PropertyMapLoader.requiredPropertiesPresent(props, DB_SERVER_NAME_KEY, DB_SERVER_PORT_KEY, DB_NAME_KEY)) {
				throw new ConfigurationRuntimeException("Since URL is absent, >0 required database properties (servername, port, dbname) not present.");
			}
			if (!NumberUtils.isDigits(props.get(DB_SERVER_PORT_KEY))) {
				throw new ConfigurationRuntimeException("Database port must be an integer.");
			}
			init(DatabaseType.valueOf(props.get(DB_TYPE_KEY)),
			     props.get(DB_SERVER_NAME_KEY),
			     Integer.parseInt(props.get(DB_SERVER_PORT_KEY)),
			     props.get(DB_NAME_KEY),
			     props.get(DB_LOGIN_NAME_KEY),
			     props.get(DB_LOGIN_PASSWORD_KEY),
			     usePooling);
		}
		else {
			init(DatabaseType.valueOf(props.get(DB_TYPE_KEY)),
				 props.get(DB_CONNECTION_URL_KEY),
				 props.get(DB_LOGIN_NAME_KEY),
				 props.get(DB_LOGIN_PASSWORD_KEY),
				 usePooling);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataSourceConfig) {
			DataSourceConfig dci = (DataSourceConfig)obj;
			return (_dbType.equals(dci._dbType) &&
					_url.equals(dci._url) &&
				    _user.equals(dci._user) &&
				    _password.equals(dci._password) &&
				    _isPooled == dci._isPooled);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		  .append(_dbType)
		  .append(_url)
		  .append(_user)
		  .append(_password)
		  .append(_isPooled)
		  .toHashCode();
	}

	public DatabaseType getDbType() {
		return _dbType;
	}

	public String getUrl() {
		return _url;
	}

	public String getServer() {
		return _server;
	}

	public int getPort() {
		return _port;
	}

	public String getDbName() {
		return _dbName;
	}

	public String getUser() {
		return _user;
	}

	public String getPassword() {
		return _password;
	}

	public boolean isPooled() {
		return _isPooled;
	}
}
