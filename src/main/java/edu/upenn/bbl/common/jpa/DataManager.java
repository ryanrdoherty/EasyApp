package edu.upenn.bbl.common.jpa;

/**
 * This interface, coupled with the JpaDataManager base class, allows for transaction-aware
 * database operations using JPA.  See example in DbWorker for details.
 * 
 * @author rdoherty
 */
public interface DataManager {
	
	/**
	 * Sets up classes needed to interface with the database, then begins a
	 * database transaction and calls doDbTasks() on the passed DbWorker.
	 * When that method returns, ends the transaction, either commiting when
	 * successful or rolling back if any exception was thrown from doDbTasks().
	 * 
	 * @param <T> implementation of DataManager
	 * @param iface an instance of a DbWorker for that DataManager type
	 * @throws Exception after rolling back the transaction, rethrows any
	 * exception that occurs in doDbTasks()
	 */
	<T extends DataManager> void doUpdates(DbWorker<T> iface) throws Exception;
	
	/**
	 * Saves a JPA entity that implements Identifiable to the database.  The
	 * argument will be merged if it represents an existing record (i.e. already
	 * has an ID), or persisted if it is a new record (no ID yet).  There are
	 * a few requirements for this to work:
	 * <ul>
	 *   <li>The object must be a JPA entity</li>
	 *   <li>The object's class must be configured to generate its ID</li>
	 * </ul>
	 * 
	 * @param <T> class of the object
	 * @param obj instance of that class
	 * @return "attached" instance
	 */
	public <T extends Identifiable> T saveObject(T obj);

	/**
	 * Deletes the record represented by the passed object.  If the object is
	 * detached, it will be looked up using its ID and removed.
	 * 
	 * @param <T> class of object to be removed
	 * @param obj object to be removed
	 * @throws IllegalArgumentException if object passed does not have an ID or does not exist
	 */
	public <T extends Identifiable> void deleteObject(T obj);
	
	/**
	 * Looks up an instance of any class implementing Identifiable by its ID
	 * and returns it.
	 * 
	 * @param <T> class of object to be retrieved
	 * @param obj class of object to be retrieved
	 * @param id id of object to be retrieved
	 * @return object retrieved
	 */
	public <T extends Identifiable> T findById(Class<T> obj, Long id);

}
