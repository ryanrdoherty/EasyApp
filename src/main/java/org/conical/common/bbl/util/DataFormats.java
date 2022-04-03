package org.conical.common.bbl.util;

import org.apache.commons.lang3.StringUtils;

public class DataFormats {
	
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
	
	/**
	 * Returns the uppercase value of the passed value, or null if
	 * null is passed in.
	 * 
	 * @param value value to be uppercased
	 * @return uppercase value or null
	 */
	public static String nullSafeUpper(String value) {
		return (value == null ? null : value.toUpperCase());
	}
}
