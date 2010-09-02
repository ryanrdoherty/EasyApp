package edu.upenn.bbl.common.enums;

import edu.upenn.bbl.common.enums.ifc.Coded;
import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Enum keeps track of the variety of races the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum Race implements Named, Described, Coded {
    WHITE(1, "White"),
    BLACK(2, "Black/African American"),
    NATIVE(3, "American Indian/Alaskan Native"),
    ASIAN(4, "Asian"),
    COMBO(5, "More than one race"),
    HAWAIIAN(6, "Hawaiian/Pacific Islander"),
    UNKNOWN(9, "Unknown/Unreported");

    private int _code;
    private String _description;
    
    private Race(int code, String description) {
    	_code = code;
    	_description = description;
    }
    
	/** {@inheritDoc} */ @Override public int getCode(){ return _code; }
	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }
    
	public static Race valueOf(int code) {
		for (Race obj : Race.values()) {
			if (obj.getCode() == code) {
				return obj;
			}
		}
		throw new IllegalArgumentException("No enum exists for this code ("+code+")");
	}
}
