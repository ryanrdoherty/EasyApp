package edu.upenn.bbl.common.exception;

/**
 * Lab's parent runtime exception.  Generally thrown to wrap
 * declared exceptions and add case-specific messages when
 * they occur.
 * 
 * @author rdoherty
 */
public class BBLRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 20100528L;
	
	public BBLRuntimeException(Exception cause) {
		super(cause);
	}

	public BBLRuntimeException(String msg, Exception cause) {
		super(msg, cause);
	}

	public BBLRuntimeException(String msg) {
		super(msg);
	}

}
