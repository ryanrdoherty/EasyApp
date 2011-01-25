package edu.upenn.bbl.common.web.struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.util.StrutsTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * Struts Date converter, used to convert Dates to Strings (to be used in web forms), and back.
 * 
 * @author rdoherty
 */
public class DateTypeConverter extends StrutsTypeConverter {
	
	private static final Logger LOG = LoggerFactory.getLogger(DateTypeConverter.class.getName());
	
	/**
	 * This format is useful because it is the default toString format for java.sql.Date,
	 * which is frequently the type of date being converted.
	 */
	private static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	
	/**
	 * Returns the format of the String to convert to or be created from a Date.
	 * This method should be overridden in child classes that prefer a different
	 * date format than the default.
	 * 
	 * @return date format
	 */
	protected String getDateFormatString() {
		return DATE_FORMAT_STR;
	}
	
	/**
	 * Converts a string in the format of getDateFormatString() to a <code>java.util.Date</code>
	 * object.  If the value is misformatted, a TypeConversionException is thrown.
	 * 
	 * @param map property map
	 * @param values input string, enclosed in an array
	 * @param toClass type of class expected to be returned
	 * @return Date or null if string is null or in an incorrect format
	 * @throws TypeConversionException if any of the values cannot be formatted correctly
	 * @throws IllegalArgumentException if toClass is not java.util.Date, or if values contains 0 or more than 1 elements
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object convertFromString(Map map, String[] values, Class toClass) {
		LOG.info("convertFromString being called with: " + map + ", [" +
				StringUtils.join(values, ",") + "], " + toClass + " and using " + getDateFormatString());
		if (!toClass.equals(Date.class)) {
			LOG.error("Bad Class");
			throw new IllegalArgumentException(
					"This method only converts to " + Date.class.getName());
		}
		SimpleDateFormat formatter = new SimpleDateFormat(getDateFormatString());
		if (values.length != 1) {
			throw new IllegalArgumentException(
					"This method only accepts a single value in the array, not " + values.length);
		}			
		try {
			if (StringUtils.isEmpty(values[0])) {
				return null;
			}
			Date d = formatter.parse(values[0]);
			LOG.info("Will return " + d);
			return d;
		}
		catch (ParseException pe) {
			LOG.error("Unable to convert the following value using (" + 
					getDateFormatString() + "): " + values[0]);
			throw new TypeConversionException(
					"Unable to convert the following value using (" + 
					getDateFormatString() + "): " + values[0], pe);
		}
	}
	
	/**
	 * Converts a java.util.Date to a String in the format of getDateFormatString().
	 * 
	 * @param map property map
	 * @param value value to be converted
	 * @return passed date formatted as String
	 * @throws IllegalArgumentException if value is not of type java.util.Date
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String convertToString(Map map, Object value) {
		LOG.info("convertToString being called with: " + map + ", " + value + ", " + value.getClass() + " and using " + getDateFormatString());
		if (value == null) {
			return "";
		}
		if (value instanceof Date) {
			SimpleDateFormat formatter = new SimpleDateFormat(getDateFormatString());
			return formatter.format((Date)value);
		}
		throw new IllegalArgumentException(
				"This method only converts objects of type " + Date.class.getName());
	}

}
