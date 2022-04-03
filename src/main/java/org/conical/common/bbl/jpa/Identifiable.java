package org.conical.common.bbl.jpa;

/**
 * Serves as an interface that all BBL JPA entities should implement.
 * This is not only good practice (so all records in the database have
 * unique IDs) but also makes it easy to implement data managers that
 * can persist, find, and merge multiple entities.
 * 
 * @author rdoherty
 */
public interface Identifiable {

	/**
	 * Returns the unique ID of this entity
	 * @return unique ID of this entity
	 */
	public Long getId();
	
	/**
	 * Sets the unique ID of this entity (should only really be used by EntityManagers).
	 * @param id unique ID to set
	 */
	public void setId(Long id);
	
}
