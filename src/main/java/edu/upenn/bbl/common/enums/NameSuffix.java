package edu.upenn.bbl.common.enums;

import org.apache.commons.lang.StringUtils;

import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Enum keeps track of the variety of name suffixes the BBL documents and their attributes
 * 
 * @author rdoherty
 */
public enum NameSuffix implements Named, Described {
	SR("Sr."),
	JR("Jr."),
	II("II"),
	III("III"),
	IV("IV"),
	MD("MD"),
	PNP("PNP");
	
	private String _description;
	
	private NameSuffix(String description) {
		_description = description;
	}
	
	/** {@inheritDoc} */ @Override public String getName() { return name(); }
	/** {@inheritDoc} */ @Override public String getDescription() { return _description; }

	public static NameSuffix resolveByDescription(String string) {
		if (StringUtils.isEmpty(string)) {
			return null;
		}
		for (NameSuffix obj : NameSuffix.values()) {
			String desc = obj.getDescription();
			if (desc.equalsIgnoreCase(string) ||
			    (desc.endsWith(".") &&
			     desc.substring(0, desc.length()-1).equalsIgnoreCase(string))) {
				return obj;
			}
		}
		throw new IllegalArgumentException("No name suffix has description '" + string + "'");
	}
}
