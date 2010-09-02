package edu.upenn.bbl.common.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import edu.upenn.bbl.common.exception.BBLRuntimeException;

/**
 * Factory class that ensures only one DataSource is created for each set of
 * database connection information.  A cache of DataSources is maintained and
 * when a DataSource is requested, the cache is consulted before creating a
 * new DataSource.  This class is threadsafe and uses DbConnectionBroker as the
 * underlying connection pooling mechanism.
 * 
 * @author rdoherty
 */
public class DataSourceFactory {
	
	private static final int DB_MIN_CONNECTIONS = 5;
	private static final int DB_MAX_CONNECTIONS = 20;
	private static final int DB_MAX_CONNECTION_TIME_MS = 600000;
		
	private static Map<DataSourceConfig, DataSource> _dataSourceMap = new HashMap<DataSourceConfig, DataSource>();
	
	/**
	 * Fetches an Oracle DataSource given the passed properties.
	 * 
	 * @param config data source configuration
	 * @return DataSource generated with those properties
	 */
	public static DataSource getDataSource(DataSourceConfig config) {
		try {
			DataSource ds = _dataSourceMap.get(config);
			if (ds == null) {
				ds = getOrCreateDataSource(config);
			}
			return ds;
		}
		catch (IOException ioe) {
			throw new BBLRuntimeException("Unable to establish connection pool.", ioe);
		}
	}

	private static synchronized DataSource getOrCreateDataSource(DataSourceConfig config) throws IOException {
		DataSource ds = _dataSourceMap.get(config);
		if (ds == null) {
			if (config.isPooled()) {
				ds = new DbConnectionBroker(
						config.getDbType().getDbDriverClass(),
						config.getUrl(),
						config.getUser(),
						config.getPassword(),
						DB_MIN_CONNECTIONS,
						DB_MAX_CONNECTIONS,
						DB_MAX_CONNECTION_TIME_MS);
			}
			else {
				ds = getUnpooledDataSource(config);
			}
			_dataSourceMap.put(config, ds);
		}
		return ds;
	}
	
	private static DataSource getUnpooledDataSource(DataSourceConfig config) {
		try {
			// create an anonymous class here so we get the application classloader rather than the system's;
			// searches for classes not found there should bubble up to the system classloader if necessary
			ClassLoader loader = (new Object(){}).getClass().getClassLoader();
			@SuppressWarnings("unchecked")
			Class<? extends DataSource> clazz =
			  (Class<? extends DataSource>)loader.loadClass(config.getDbType().getDataSourceClass());
			DataSource ds = clazz.newInstance();
			setProperty(ds, "setURL", config.getUrl());
			setProperty(ds, "setUser", config.getUser());
			setProperty(ds, "setPassword", config.getPassword());
			return ds;
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not find DB-specific DataSource", e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("Could not access DB-specific DataSource", e);
		}
		catch (InstantiationException e) {
			throw new RuntimeException("Could not instantiate DB-specific DataSource", e);
		}
	}
	
	private static void setProperty(Object obj, String methodName, String arg) {
		try {
			obj.getClass().getMethod(methodName, String.class).invoke(obj, arg);
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to execute method " + methodName + " on DB-specific DataSource.");
		}
	}
}
