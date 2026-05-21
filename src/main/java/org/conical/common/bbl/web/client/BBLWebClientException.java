package org.conical.common.bbl.web.client;

import org.conical.common.bbl.exception.BBLRuntimeException;

/**
 * This is a runtime exception meant for use by web service client applications.
 * 
 * @author rdoherty
 */
public class BBLWebClientException extends BBLRuntimeException {

	private static final long serialVersionUID = 1L;

	public BBLWebClientException(Exception cause) {
		super(cause);
	}
	
	public BBLWebClientException(String message) {
		super(message);
	}
	
	public BBLWebClientException(String message, Exception cause) {
		super(message, cause);
	}

}
