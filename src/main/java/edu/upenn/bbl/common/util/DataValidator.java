package edu.upenn.bbl.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class which can serve as a parent to any classes that need to
 * validate input data. (e.g. when processing form input).  Typically a child
 * class will want to call the validator methods using a <code>label</code>
 * with the first letter capitalized, since the validation messages generally
 * start with the label.
 * 
 * @author rdoherty
 */
public abstract class DataValidator {

	private static final Logger LOG = LoggerFactory.getLogger(DataValidator.class.getName());

	/**
	 * This format is useful because it is the default Struts2 converted format (i.e. when
	 * struts populates text fields with dates, this is the default format displayed).
	 */
	protected static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	
	private List<String> _validationErrors = new ArrayList<String>();
	
	public static Date convertDate(String date) {
		try {
			return new SimpleDateFormat(DATE_FORMAT_STR).parse(date);
		}
		catch (ParseException e) {
			LOG.error("Unable to parse date input: " + date);
			return null;
		}
	}
	
	/**
	 * Returns whether the field is empty, regardless of type.
	 * 
	 * @param value value to be evaluated
	 * @return true if value is null, or if, after conversion to a
	 * String, value contains no characters
	 */
	public static boolean isFieldEmpty(Object value) {
		return (value == null || StringUtils.isEmpty(String.valueOf(value)));
	}
	
	/**
	 * Adds a validation error or warning to the message list
	 * 
	 * @param errorStr error/warning text
	 */
	protected void addValidationError(String errorStr) {
		_validationErrors.add(errorStr);
	}

	/**
	 * Returns a list of validation errors/warnings encountered while validating
	 * 
	 * @return list of validation errors/warnings
	 */
	public List<String> getErrors() {
		return _validationErrors;
	}
	
	/**
	 * Returns true if the passed value is <= maxLength, or if value is null or empty.
	 * Otherwise returns false.  If false is returned, an error message is added detailing
	 * the validation failure.
	 * 
	 * @param label name of the value passed
	 * @param value value to be validated
	 * @param maxLength maximum valid length of the value
	 * @return true if value is blank or valid, else false
	 */
	protected boolean isRequiredSize(String label, String value, int maxLength) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		if (value.length() > maxLength) {
			_validationErrors.add(label + " must be no more than " + maxLength + " characters.");
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the passed value is <= maxLength and >= minLength, or if value is
	 * null or empty.  Otherwise returns false.  If false is returned, an error message
	 * is added detailing the validation failure.
	 * 
	 * @param label name of the value passed
	 * @param value value to be validated
	 * @param minLength minimum valid length of the value
	 * @param maxLength maximum valid length of the value
	 * @return true if value is blank or valid, else false
	 */
	protected boolean isRequiredSize(String label, String value, int minLength, int maxLength) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		if (value.length() > maxLength || value.length() < minLength) {
			if (minLength == maxLength) {
				_validationErrors.add(label + " must be exactly " + minLength + " characters.");
			}
			else {
				_validationErrors.add(label + " must be between " + minLength + " and " + maxLength + " characters (inclusive).");
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if the passed value is plain integer (only made up of digits), or
	 * is null or empty.  Otherwise returns false.  If false is returned, an error
	 * message is added detailing the validation failure.
	 * 
	 * @param label name of the value passed
	 * @param value value to be validated
	 * @return true if value is blank or valid, else false
	 */
	protected boolean isIntegerAsRequired(String label, String value) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		if (!NumberUtils.isDigits(value)) {
			_validationErrors.add(label + " must be an integer value.");
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the passed value can be evaluated to be a number, or
	 * is null or empty.  Otherwise returns false.  If false is returned, an error
	 * message is added detailing the validation failure.
	 * 
	 * @param label name of the value passed
	 * @param value value to be validated
	 * @return true if value is blank or valid, else false
	 */
	protected boolean isFloatAsRequired(String label, String value) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		if (!NumberUtils.isNumber(value)) {
			_validationErrors.add(label + " must be an numerical value.");
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the passed value is null or empty.  Otherwise returns false.
	 * If true is returned, an error message is added detailing the validation failure.
	 * 
	 * @param label name of the value passed
	 * @param value value to be validated
	 * @return true if value is null or blank, else false
	 */
	protected boolean isRequiredFieldEmpty(String label, Object value) {
		if (isFieldEmpty(value)) {
			_validationErrors.add(label + " is required.");
			return true;
		}
		return false;
	}
}
