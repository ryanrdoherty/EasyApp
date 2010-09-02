package edu.upenn.bbl.common.auth;

/**
 * Authenticates users and assigns access roles based on LDAP queries. This
 * implementation is incomplete and DOES NOT WORK!  Do not use.
 * 
 * TODO: implement
 * 
 * @author rdoherty
 */
public class LdapAuthenticator implements Authenticator {

	public LdapAuthenticator(AuthConfig config) throws AuthenticationException {
		throw new UnsupportedOperationException("Implementation is incomplete.  This class should not be used.");
	}

	@Override
	public User getAuthenticatedUser(String username, String password)
			throws AuthenticationException {
		throw new UnsupportedOperationException("Implementation is incomplete.  This class should not be used.");
	}
	
	@Override
	public User getUnauthenticatedUser(String username)
			throws AuthenticationException {
		throw new UnsupportedOperationException("Implementation is incomplete.  This class should not be used.");
	}
	
	@Override
	public boolean isValidUser(String username, String password)
			throws AuthenticationException {
		throw new UnsupportedOperationException("Implementation is incomplete.  This class should not be used.");
	}

	@Override
	public boolean userHasRole(String username, String role)
			throws AuthenticationException {
		throw new UnsupportedOperationException("Implementation is incomplete.  This class should not be used.");
	}



}
