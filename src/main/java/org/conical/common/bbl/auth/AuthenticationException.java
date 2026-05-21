package org.conical.common.bbl.auth;

/**
 * Exception thrown to indicate a problem with authentication.
 * 
 * @author rdoherty
 */
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 20100526L;
	
	public AuthenticationException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AuthenticationException(String msg) {
		super(msg);
	}
}
