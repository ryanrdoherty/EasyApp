package edu.upenn.bbl.common.auth;

/**
 * This interface defines an API that can be used to authenticate users and check
 * for access roles.
 * 
 * @author rdoherty
 *
 */
public interface Authenticator {

	/**
	 * Checks the password for the passed user.  If valid, returns user attributes
	 * and all known roles.  If not, returns null.  Note that the caller is
	 * responsible for encrypting the password using the same algorithm used to store
	 * the password originally.  No password transformation is performed.
	 * 
	 * @param username username to check
	 * @param password password to check
	 * @return authenticated user, or null
	 * @throws AuthenticationException if an error occurs while authenticating
	 */
	public User getAuthenticatedUser(String username, String password) throws AuthenticationException;

	/**
	 * Looks up user attributes <strong>without authenticating</strong>, and returns them.  If no
	 * user with the passed name can be found, returns null
	 * 
	 * @param username user for which to search
	 * @return unauthenticated user, or null
	 * @throws AuthenticationException if an error occurs while searching for the user
	 */
	public User getUnauthenticatedUser(String username) throws AuthenticationException;
	
	/**
	 * Checks if the given username and password represents a valid user.
	 * Same rules as <code>getAuthenticatedUser()</code>.
	 * 
	 * @param username username to check
	 * @param password to check
	 * @return true if valid user, else false
	 * @throws AuthenticationException if an error occurs while authenticating
	 */
	public boolean isValidUser(String username, String password) throws AuthenticationException;

	/**
	 * Returns true if user has the passed role, else false.
	 * 
	 * @param username user to check
	 * @param role role to check
	 * @return true if user has role, else false
	 * @throws AuthenticationException if an error occurs while looking up user or roles
	 */
	public boolean userHasRole(String username, String role) throws AuthenticationException;

}
