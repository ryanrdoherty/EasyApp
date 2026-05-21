package org.conical.common.bbl.db;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.conical.common.bbl.exception.BBLRuntimeException;

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
	 * Looks up a DataSource in JNDI registered under the passed name and returns it.
	 * Note: This assumes the Tomcat implementation of JNDI.
	 * 
	 * @param jndiName name with which data source should be looked up
	 * @return data source registered through JNDI
	 * @throws BBLRuntimeException if no DataSource is configured with that name
	 */
	public static DataSource getJndiDataSource(String jndiName) {
		try {
			Context context = new InitialContext();
			Context envContext = (Context) context.lookup("java:comp/env");
			DataSource ds = (DataSource)envContext.lookup(jndiName);
			return ds;
		}
		catch (NamingException ne) {
			throw new BBLRuntimeException("Unable to look up DataSource using JNDI name " + jndiName, ne);
		}
	}
	
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
			DataSource ds = clazz.getConstructor().newInstance();
			setProperty(ds, "setURL", config.getUrl());
			setProperty(ds, "setUser", config.getUser());
			setProperty(ds, "setPassword", config.getPassword());
			return ds;
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Could not initialize DB-specific DataSource", e);
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
