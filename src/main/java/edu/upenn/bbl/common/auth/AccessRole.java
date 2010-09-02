package edu.upenn.bbl.common.auth;

import edu.upenn.bbl.common.enums.ifc.Described;
import edu.upenn.bbl.common.enums.ifc.Named;

/**
 * Values in this enum represent access roles that can be given
 * to users.
 * 
 * @author rdoherty
 */
public enum AccessRole implements Named, Described {
	ADMIN             ("Can edit users/passwords, perform writes through the SQL interface"),
	COORDINATOR       ("General permissions of a research coordinator"),
	GO_MANUAL_ENTRY   ("Access to the GO Manual Entry web app"),
	MED_LEGAL_WRITE   ("Able to write Medical Legal data"); // no special permissions required to read
		
	private String _description;
	
	private AccessRole(String description) {
		_description = description;
	}

	/**
	 * Convenience method to provide getter access to name().
	 */
	@Override
	public String getName() {
		return name();
	}

	/**
	 * Tells what this role grants access to.
	 */
	@Override
	public String getDescription() {
		return _description;
	}
}
