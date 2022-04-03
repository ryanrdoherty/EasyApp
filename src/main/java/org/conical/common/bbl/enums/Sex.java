package org.conical.common.bbl.enums;

import org.conical.common.bbl.enums.ifc.Coded;
import org.conical.common.bbl.enums.ifc.Described;
import org.conical.common.bbl.enums.ifc.Named;

/**
 * Enum keeps track of the variety of sexes the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum Sex implements Named, Described, Coded {
	MALE(1, "Male"),
	FEMALE(2, "Female"),
	OTHER(3, "Other");
	
	private int _code;
	private String _description;
	
	private Sex(int code, String description) {
		_code = code;
		_description = description;
	}
	
	/** {@inheritDoc} */ @Override public int getCode(){ return _code; }
	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }

	public static Sex valueOf(int code) {
		for (Sex obj : Sex.values()) {
			if (obj.getCode() == code) {
				return obj;
			}
		}
		throw new IllegalArgumentException("No enum exists for this code ("+code+")");
	}

	@SuppressWarnings("incomplete-switch")
	public Sex getOpposite() {
		switch(this) {
			case MALE: return FEMALE;
			case FEMALE: return MALE;
		}
		throw new UnsupportedOperationException("Only male and female sexes have opposites.");
	}
}
