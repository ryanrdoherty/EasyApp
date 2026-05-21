package edu.upenn.bbl.common.enums;

import edu.upenn.bbl.common.enums.ifc.Coded;
import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Enum keeps track of the variety of handednesses the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum Handedness implements Named, Described, Coded {
	RIGHT("Right", 1),
	LEFT("Left", 2),
	AMBIDEXTROUS("Ambidextrous", 3),
	UKNOWN("Unknown", 9);

	private String _description;
	private int _code;
	
	private Handedness(String description, int code) {
		_description = description;
		_code = code;
	}

	/** {@inheritDoc} */ @Override public int getCode(){ return _code; }
	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }
}
