package edu.upenn.bbl.common.auth;

/**
 * Responsible for returning an implementation of Authenticator appropriate for the
 * given <code>AuthConfig</code>.
 * 
 * @author rdoherty
 */
public class AuthFactory {

	private static final String AUTH_DATASOURCE_JNDI_NAME = "jdbc/AuthenticationDS";
	
	/**
	 * Returns the default Authenticator, which connects to a database using the
	 * BBL standard JNDI name under which a data source to the BBL authentication
	 * database should be registered.
	 * 
	 * @return default Authenticator
	 * @throws AuthenticationException if error occurs creating Authenticator
	 */
	public static Authenticator getAuthenticator() throws AuthenticationException {
		return new DatabaseAuthenticator(AUTH_DATASOURCE_JNDI_NAME);
	}
	
	/**
	 * Checks config and returns an appropriate <code>Authenticator</code>.
	 * 
	 * @param config config for which to create an Authenticator
	 * @return appropriate Authenticator
	 * @throws AuthenticationException if error occurs creating Authenticator
	 * @throws UnsupportedOperationException if AuthConfig type is not supported
	 */
	public static Authenticator getAuthenticator(AuthConfig config) throws AuthenticationException {
		switch(config.getType()) {
			case DATABASE:
				return new DatabaseAuthenticator(config);
			case LDAP:
				return new LdapAuthenticator(config);
			default:
				throw new UnsupportedOperationException(
					"Auth config type " + config.getClass() + " is not yet supported.");
		}
	}
}
