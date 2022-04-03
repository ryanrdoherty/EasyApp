package org.conical.common.bbl.auth;

import java.util.Comparator;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conical.common.bbl.exception.ConfigurationRuntimeException;

/**
 * Holds values used to configure and determine how an <code>Authenticator</code>
 * retrieves access roles and other verification information for applications.
 * 
 * @author rdoherty
 *
 */
public class AuthConfig implements Comparator<AuthConfig>, Comparable<AuthConfig> {

  private static final Logger LOG = LogManager.getLogger(AuthConfig.class);
  
	/**
	 * An authorization source type (what type of resource is
	 * being queried for authorization credentials).
	 * 
	 * @author rdoherty
	 */
	public enum Type {
		DATABASE,
		LDAP,
		CUSTOM_CLASS;
	}

	public static final String AUTH_TYPE = "auth.type";
	public static final String AUTH_CONNECTION_URL = "auth.connection.url";
	public static final String AUTH_NAME = "auth.login.name";
	public static final String AUTH_PASSWORD = "auth.password";
	public static final String AUTH_CLASS = "auth.classname";
	
	private Type _type;
	private String _connectionUrl;
	private String _loginName;
	private String _password;
	private String _authClassName;
	
	/**
	 * No-param constructor.  User is required to set attributes.
	 */
	public AuthConfig() {
		// do nothing; user must set all params with this constructor
	}

	/**
	 * Constructor takes a resource bundle name and parses it for required parameters
	 * 
	 * @param bundleName name of bundle from which params will be pulled
	 */
	public AuthConfig(String bundleName) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
			setType(AuthConfig.Type.valueOf(bundle.getString(AUTH_TYPE).toUpperCase()));
			if (Type.DATABASE.equals(_type)) {
			  setConnectionUrl(bundle.getString(AUTH_CONNECTION_URL));
			  setLoginName(bundle.getString(AUTH_NAME));
			  setPassword(bundle.getString(AUTH_PASSWORD));
			}
			else if (Type.CUSTOM_CLASS.equals(_type)) {
			  setAuthClassName(bundle.getString(AUTH_CLASS));
			}
			assertComplete();
		}
		catch (AuthenticationException ae) {
			throw new ConfigurationRuntimeException("Unable to properly configure authentication", ae);
		}
	}
	
	/**
	 * @return authorization type for this configuration
	 */
	public Type getType() {
		return _type;
	}
	
	/**
	 * @param type type to set on this configuration
	 */
	public void setType(Type type) {
		_type = type;
	}
	
	/**
	 * @return connection type for this configuration
	 */
	public String getConnectionUrl() {
		return _connectionUrl;
	}
	
	/**
	 * @param connectionUrl url to set for this configuration
	 */
	public void setConnectionUrl(String connectionUrl) {
		_connectionUrl = connectionUrl;
	}

	/**
	 * @return login name for this configuration
	 */
	public String getLoginName() {
		return _loginName;
	}
	
	/**
	 * @param loginName login name to set for this configuration
	 */
	public void setLoginName(String loginName) {
		_loginName = loginName;
	}

	/**
	 * @return password for this configuration
	 */
	public String getPassword() {
		return _password;
	}
	
	/**
	 * @param password password to set for this configuration
	 */
	public void setPassword(String password) {
		_password = password;
	}

	/**
	 * @return class name of custom authenticator
	 */
	public String getAuthClassName() {
		return _authClassName;
	}

	/**
	 * @param authClassName custom authentication class for this configuration
	 */
	public void setAuthClassName(String authClassName) {
		_authClassName = authClassName;
	}
  
	/**
	 * Creates a copy of this object and returns it
	 * 
	 * @return copy with identical config params
	 */
	public AuthConfig createCopy() {
		AuthConfig config = new AuthConfig();
		config.setType(_type);
		config.setConnectionUrl(_connectionUrl);
		config.setLoginName(_loginName);
		config.setPassword(_password);
		config.setAuthClassName(_authClassName);
		return config;
	}
	
	/**
	 * Implementation of compare to allow sorting of AuthConfigs
	 */
	@Override
	public int compare(AuthConfig o1, AuthConfig o2) {
		if (o1._type.compareTo(o2._type) != 0) {
			return o1._type.compareTo(o2._type);
		}
		if (o1._connectionUrl.compareTo(o2._connectionUrl) != 0) {
			return o1._connectionUrl.compareTo(o2._connectionUrl);
		}
		if (o1._loginName.compareTo(o2._loginName) != 0) {
			return o1._loginName.compareTo(o2._loginName);
		}
		if (o1._password.compareTo(o2._password) != 0) {
			return o1._password.compareTo(o2._password);
		}
		if (o1._authClassName.compareTo(o2._authClassName) != 0) {
		  return o1._authClassName.compareTo(o2._authClassName);
		}
		return 0;
	}

	/**
	 * Implementation of compareTo to allow sorting of AuthConfigs.
	 * Simply calls compare(this, o).
	 */
	@Override
	public int compareTo(AuthConfig o) {
		return compare(this, o);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AuthConfig)) {
			return false;
		}
		return (compareTo((AuthConfig)o) == 0);
	}
	
	/**
	 * Override of hashCode to complement equals()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	/**
	 * Asserts that properties of this configuration have all been set.
	 * 
	 * @throws AuthenticationException if they have not
	 */
	public void assertComplete() throws AuthenticationException {
		// for now, just make sure type is database and the other properties are set
		if (_type.equals(Type.DATABASE) &&
			(StringUtils.isEmpty(_connectionUrl) ||
			 StringUtils.isEmpty(_loginName) ||
			 StringUtils.isEmpty(_password))) {
			throw new AuthenticationException("Not all parameters have been set for this configuration.");
		}
		else if (_type.equals(Type.CUSTOM_CLASS) && !authClassExists(_authClassName)) {
			throw new AuthenticationException("Unable to load Authenticator class: " + _authClassName);
		}
		else if (_type.equals(Type.LDAP)){
			throw new AuthenticationException("LDAP is not a supported authentication strategy at this time.");
		}
	}

	private boolean authClassExists(String authClassName) {
		try {
			Class<?> clazz = Class.forName(authClassName);
			if (!Authenticator.class.isAssignableFrom(clazz)) {
				LOG.warn("Configured authenticator class " + clazz.getName() +
						" found, but is not an Authenticator.");
				return false;
			}
			return true;
		}
		catch(ClassNotFoundException e) {
			return false;
		}
	}
	
}
