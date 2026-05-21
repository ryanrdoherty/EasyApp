package org.conical.common.bbl.enums.ifc;

/**
 * This is a convenience interface that all BBL enums should implement.  The
 * purpose is for all enums to have a "description" attribute, since a lot of
 * Java frameworks (e.g. Struts) use attribute names + reflection to retrieve
 * data from objects.  The returned value should be a nice, human-readable value
 * and/or description telling what that particular enum value means.
 * 
 * @author rdoherty
 */
public interface Described {

	/**
	 * Returns a human-readable description of what this value
	 * is/means or perhaps how it is used
	 * 
	 * @return enum value description
	 */
	public String getDescription();
}
