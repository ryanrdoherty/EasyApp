package edu.upenn.bbl.common.enums;

import edu.upenn.bbl.common.enums.ifc.Coded;
import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Enum keeps track of the variety of ethnicities the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum Ethnicity implements Named, Described, Coded {
	
	HISPANIC(1, "Hispanic"),
	NON_HISPANIC(2, "Non-Hispanic"),
	UNKNOWN(9, "Unknown");
	
	private int _code;
	private String _description;
	
	private Ethnicity(int code, String description) {
		_code = code;
		_description = description;
	}
	
	/** {@inheritDoc} */ @Override public int getCode(){ return _code; }
	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }
	
	public static Ethnicity valueOf(int code) {
		for (Ethnicity obj : Ethnicity.values()) {
			if (obj.getCode() == code) {
				return obj;
			}
		}
		throw new IllegalArgumentException("No enum exists for this code ("+code+")");
	}
}
