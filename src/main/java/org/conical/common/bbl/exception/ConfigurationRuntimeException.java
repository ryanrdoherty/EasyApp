package org.conical.common.bbl.exception;

/**
 * Runtime exception used to denote errors occuring during startup and
 * configuration processing. (e.g. bad file format, DB connection failure,
 * start-up JNDI lookup failures).
 * 
 * @author rdoherty
 */
public class ConfigurationRuntimeException extends BBLRuntimeException {

	private static final long serialVersionUID = 20100528L;

	public ConfigurationRuntimeException(Exception cause) {
		super(cause);
	}

	public ConfigurationRuntimeException(String msg, Exception cause) {
		super(msg, cause);
	}
	
	public ConfigurationRuntimeException(String msg) {
		super(msg);
	}
}
