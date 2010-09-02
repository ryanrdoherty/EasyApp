package edu.upenn.bbl.common.jpa;

/**
 * Performs database tasks using a given the operations available on
 * the passed DataManager.  Typical usage is to subclass a child of DbWorker
 * inline and perform the required operations inside an overridden doDbTasks
 * method.  For example, if you have:
 * <ul>
 *   <li><pre>interface AppDataManager extends DataManager</pre></li>
 *   <li><pre>class AppDataManagerImpl implements AppDataManager extends JpaDataManager</pre></li>
 *   <li><pre>class AppDbWorker extends DbWorker&lt;AppDataManager&gt;</pre></li>
 * </ul>
 * Then you can do the following in your code to perform DB tasks within a transaction:
 * <pre>
 *   
 *   new AppDbWorker() {
 *     public void doDbTasks(AppDataManager dataMgr) {
 *     
 *       // perform desired database operations using your custom DataManager
 *     
 *     }
 *   }.doWork();
 * </pre>
 * 
 * @author rdoherty
 *
 * @param <T> An implementation of DataManager (likely a subclass of JpaDataManager)
 */
public abstract class DbWorker<T extends DataManager> {

	/**
	 * Performs work defined in doDbTasks.  What operations are performed
	 * are implementation dependent.
	 * 
	 * @throws Exception if an error occurs
	 */
	public final void doWork() throws Exception {
		T dataMgr = getDataManager();
		dataMgr.doUpdates(this);
	}

	/**
	 * Returns an instance of the subclass's implementation of DataManager
	 * 
	 * @return DataManager instance
	 */
	public abstract T getDataManager();
	
	/**
	 * Must be overridden.  Performs desired database operations.
	 * 
	 * @param dataManager DataManager subclass that can perform desired operations
	 */
	public abstract void doDbTasks(T dataManager);
	
}
