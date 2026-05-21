package edu.upenn.bbl.common.enums.ifc;

/**
 * This interface helps ensure that enum values that have integer equivalents
 * (usually stored in database tables) are easily distinguishable from those
 * that aren't.  The return value for the lone method should be the int value
 * stored when this enum value is chosen.
 * 
 * @author rdoherty
 */
public interface Coded {

	/**
	 * Returns a integer code for this enum value, generally the representative
	 * value stored in a database.
	 * 
	 * @return code for this value
	 */
	public int getCode();
}
