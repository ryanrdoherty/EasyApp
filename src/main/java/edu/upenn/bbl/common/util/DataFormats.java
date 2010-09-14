package edu.upenn.bbl.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFormats {

	private static final Logger LOG = LoggerFactory.getLogger(DataFormats.class.getName());
	
	/**
	 * This format is useful because it is the default Struts2 converted format (i.e. when
	 * struts populates text fields with dates, this is the default format displayed).
	 */
	protected static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	
	/**
	 * Converts a string in the format YYYY-MM-DD to a <code>java.util.Date</code>
	 * 
	 * @param date input string
	 * @return Date or null if string is null or in an incorrect format
	 */
	public static Date convertDate(String date) {
		if (date == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(DATE_FORMAT_STR).parse(date);
		}
		catch (ParseException e) {
			LOG.error("Unable to parse date input: " + date);
			return null;
		}
	}
	
	/**
	 * Easy way to take the components of a Name and combine
	 * them into a human-readable single string.  This is in the
	 * format "first middle last suffix.description", omitting
	 * empty or null fields.
	 * 
	 * @param name Name object
	 * @return full name as string
	 */
	public static String getFullName(Name name) {
		String first = (StringUtils.isEmpty(name.getFirstName()) ? "" : name.getFirstName());
		String mi = (StringUtils.isEmpty(name.getMiddleName()) ? "" : " " + name.getMiddleName());
		String last = (StringUtils.isEmpty(name.getLastName()) ? "" : " " + name.getLastName());
		String suffix = (name.getSuffix() == null ? "" : " " + name.getSuffix().getDescription());
		return (first + mi + last + suffix).trim();
	}
}
