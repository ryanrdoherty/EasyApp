package edu.upenn.bbl.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFormats {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DataFormats.class.getName());
	
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
