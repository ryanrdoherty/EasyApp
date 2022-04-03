package org.conical.common.bbl.enums;

import org.conical.common.bbl.enums.ifc.Described;
import org.conical.common.bbl.enums.ifc.Named;

/**
 * Enum keeps track of the variety of sexual orientations the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum SexualOrientation implements Named, Described {
	HETEROSEXUAL("Heterosexual"),
	HOMOSEXUAL("Homosexual"),
	BISEXUAL("Bisexual"),
	ASEXUAL("Asexual"),
	OTHER("Other");
	
	private String _description;

	private SexualOrientation(String description) {
		_description = description;
	}

	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }
}
