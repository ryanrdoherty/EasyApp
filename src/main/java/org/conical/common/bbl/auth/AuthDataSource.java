package org.conical.common.bbl.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.conical.common.bbl.db.DatabaseType;
import org.conical.common.bbl.db.DbConnectionBroker;

/**
 * The AuthDataSource class wraps a <code>DataSource</code> to be used in
 * authenticating users and determining credentials.  It cannot be instantiated
 * but is retrieved via the instance method, which requires an <code>AuthConfig</code>.
 * A static map is used to ensure that no more than one DB pool (as wrapped in the
 * DataSource), is created per configuration.
 * 
 * NOTE: despite the ability to pass connection URLs for other databases, currently
 * only an Oracle database is supported for database authentication
 * 
 * @author rdoherty
 */
public class AuthDataSource {
	
	private static final String DB_DRIVER = DatabaseType.ORACLE.getDbDriverClass();
	
	private static Map<AuthConfig, AuthDataSource> _dsMap = new HashMap<AuthConfig, AuthDataSource>();
	
	/**
	 * Looks up the AuthDataSource for this configuration and returns it if it exists.  If not,
	 * attempts to create one, stores it, and returns it.
	 * 
	 * @param config configuration of the desired AuthDataSource
	 * @return an AuthDataSource for the given configuration
	 * @throws AuthenticationException if unable to create a connection pool for this configuration
	 */
	public static synchronized AuthDataSource getInstance(AuthConfig config) throws AuthenticationException {
		if (!config.getType().equals(AuthConfig.Type.DATABASE)) {
			throw new IllegalArgumentException("Cannot create Auth Data Source with non-database config (" + config.getType() + ")");
		}
		AuthDataSource ads = _dsMap.get(config);
		if (ads == null) {
			try {
				ads = new AuthDataSource(new DbConnectionBroker(DB_DRIVER, config.getConnectionUrl(), config.getLoginName(),
						config.getPassword(), 5, 20, 5000));
			}
			catch (IOException ioe) {
				throw new AuthenticationException("Could not create connection pool for authentication", ioe);
			}
			_dsMap.put(config.createCopy(), ads);
		}
		return ads;
	}

	private DataSource _ds;
	
	/**
	 * Private constructor to avoid external instantiation
	 */
	private AuthDataSource() {
		// do nothing;
	}
	
	private AuthDataSource(DataSource ds) {
		_ds = ds;
	}

	/**
	 * @return DataSource wrapped by this object
	 */
	public DataSource getDataSource() {
		return _ds;
	}

}
