package edu.upenn.bbl.common.enums.ifc;

/**
 * This is a convenience interface that all BBL enums should implement.  The
 * purpose is for all enums to have a "name" attribute (not just the method), since
 * a lot of Java frameworks (e.g. Struts) use attribute names + reflection to
 * retrieve data from objects.  The implementation for your enum may vary but should
 * almost certainly return simply "name()" so that such frameworks can access the
 * name of your enum.
 * 
 * @author rdoherty
 */
public interface Named {

	/**
	 * Convenience method to provide getter access to name().
	 * 
	 * @return same as name()
	 */
	public String getName();
	
}
