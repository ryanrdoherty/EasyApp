package org.conical.common.bbl.util;

import org.conical.common.bbl.enums.NameSuffix;

/**
 * This interface can be used to identify a class that contains
 * information about a person's name.  Setters are not included
 * but may be added by implementing classes.
 * 
 * @author rdoherty
 */
public interface Name {

	/**
	 * @return first name
	 */
	public String getFirstName();

	/**
	 * @return middle name
	 */
	public String getMiddleName();

	/**
	 * @return last name
	 */
	public String getLastName();

	/**
	 * @return suffix
	 */
	public NameSuffix getSuffix();

	/**
	 * Aggregates other name data into a human-readable
	 * string.  Typically this can be implemented by returning
	 * simply <code>DataFormats.getFullName(this)</code>.
	 * 
	 * @return full name
	 */
	public String getFullName();
}
