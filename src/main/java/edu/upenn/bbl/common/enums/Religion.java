package edu.upenn.bbl.common.enums;

import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Enum keeps track of the variety of religions the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum Religion implements Named, Described {
	CHRISTIAN("Christian"),
	JEWISH("Jewish"),
	MUSLIM("Muslim"),
	HINDU("Hindu"),
	JAIN("Jain"),
	OTHER("Other"),
	NONE("None");
	
	private String _description;
	
	private Religion(String description) {
		_description = description;
	}

	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }
}
