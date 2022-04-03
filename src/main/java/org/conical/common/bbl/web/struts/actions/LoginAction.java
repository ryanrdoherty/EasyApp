package org.conical.common.bbl.web.struts.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.conical.common.bbl.auth.AuthConfig;
import org.conical.common.bbl.auth.AuthFactory;
import org.conical.common.bbl.auth.AuthenticationException;
import org.conical.common.bbl.auth.Authenticator;
import org.conical.common.bbl.auth.EncryptionUtil;
import org.conical.common.bbl.auth.User;
import org.conical.common.bbl.exception.BBLRuntimeException;

import com.opensymphony.xwork2.ActionContext;

/**
 * Attempts to log user into the application using the authentication settings in
 * authentication.properties.  A form submitting to this action should contain
 * the following parameters:
 *     username:   username
 *     password:   password for user
 *     requestUrl: URL to return to after successful authentication
 * 
 * @author rdoherty
 */
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 20100527L;

	private static Logger LOG = LogManager.getLogger(LoginAction.class);
	
	private static final String AUTH_CONFIG_BUNDLE = "authentication";

	private static Authenticator _auth = getAuthenticator();
	
	private String _username;
	private String _password;
	private String _message;
	private String _requestUrl;
	
	/**
	 * It is assumed that if user needs to log in, user is not logged in; returns false.
	 * 
	 * @return false
	 */
	@Override
	protected boolean actionRequiresLogin() {
		return false;
	}

	static Authenticator getAuthenticator() {
		try {
			AuthConfig authConfig = new AuthConfig(AUTH_CONFIG_BUNDLE);
			return AuthFactory.getAuthenticator(authConfig);
		}
		catch (Exception e1) {
			LOG.warn("Unable to look up authentication properties using bundle " +
					  "(bundle may not be present).  Will use default authenticator.", e1);
			try {
				return AuthFactory.getAuthenticator();
			}
			catch (AuthenticationException e2) {
				throw new BBLRuntimeException("Unable to create default authenticator", e2);
			}
		}
	}
	
	/**
	 * Attempts to log user into the application using the authentication settings in
     * authentication.properties.
	 * 
	 * @return success if able to log user in, else input
	 */
	@Override
	public String doWork() throws Exception {
		if (StringUtils.isEmpty(getUsername()) || StringUtils.isEmpty(getPassword())) {
			LOG.debug("User attempted to go directly to Login page, or entered empty credentials.");
			_message = "Please enter both a username and password.";
			return INPUT;
		}
		String encryptedPassword = EncryptionUtil.encrypt(getPassword(), EncryptionUtil.Algorithm.SHA);
		LOG.debug("Asking authenticator for user as defined by: " + getUsername() + " and " + encryptedPassword);
		User user = _auth.getAuthenticatedUser(getUsername(), encryptedPassword);
		if (user == null) {
			LOG.debug("Could not authenticate user:password combination " + getUsername() + ":" + encryptedPassword);
			_message = "Invalid username/password combination.  Please try again.";
			return INPUT;
		}
		setCurrentUser(user);
		for (Cookie cookie : getAdditionalCookies()) {
		  addCookie(cookie);
		}
		return SUCCESS;
	}

	public static void setCurrentUser(User user) {
		ActionContext.getContext().getSession().put(BaseAction.USER_KEY, user.getUsername());
	}

	/**
	 * Returns any additional cookies child action wishes to set.  Default
	 * behavior is to return an empty list.
	 * 
	 * @return list of cookies to set upon successful login
	 */
	protected List<Cookie> getAdditionalCookies() {
		return new ArrayList<Cookie>();
	}

	/**
	 * If authentication fails, this message will tell the user what happened
	 * 
	 * @return user message
	 */
	public String getMessage() {
		return _message;
	}
	
	/**
	 * Sets user name sent from form
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		_username = username;
	}

	/**
	 * Returns user name originally passed in
	 * 
	 * @return original user name
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * Sets password sent from form
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		_password = password;
	}

	/**
	 * Returns password originally passed in
	 * 
	 * @return original password
	 */
	public String getPassword() {
		return _password;
	}

	/**
	 * Allows pages to set the "original" request URL when they go to a
	 * login page.  By submitting this value with the login form values,
	 * they can redirect to the original page once user is logged in.  Note
	 * that if user originally goes to the login page, a nasty loop occurs;
	 * also, POST requests are not supported, so parameters will be lost and
	 * a GET to the original page will be performed, which may produce
	 * errors depending on your implementation.
	 * 
	 * @param requestUrl original request URL
	 */
	public void setRequestUrl(String requestUrl) {
		_requestUrl = requestUrl;
	}

	/**
	 * Overrides BaseAction.getRequestUrl() to return the page originally
	 * requested.  See <code>setRequestUrl()</code> for limitations.
	 * 
	 * @return original request URL, or, if not set, the URL of this request
	 */
	@Override
	public String getRequestUrl() {
		if (_requestUrl == null) {
			return super.getRequestUrl();
		}
		return _requestUrl;
	}
}
