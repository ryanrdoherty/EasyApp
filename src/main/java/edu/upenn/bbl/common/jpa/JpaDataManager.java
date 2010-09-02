package edu.upenn.bbl.common.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * Abstract implementation of DataManager that performs JPA operations within
 * a transaction.  Please note this implementation is NOT thread-safe.
 * 
 * @author rdoherty
 */
public abstract class JpaDataManager implements DataManager {

	private EntityManager _em;
	private EntityTransaction _tx;
	private boolean _allowMethodCalls = false;

	/**
	 * Returns the EntityManagerFactory that will generate EntityManagers used
	 * to perform JPA database operations.
	 * 
	 * @return configured EntityManagerFactory
	 */
	protected abstract EntityManagerFactory getEmf();

	/**
	 * Performs database operations defined in the passed worker within a transaction.
	 * 
	 * @param dbUpdater worker which will perform the actual database operations
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final <T extends DataManager> void doUpdates(DbWorker<T> dbUpdater) throws Exception {
		try {
			_em = getEmf().createEntityManager();
			_tx = _em.getTransaction();
			_tx.begin();
			_allowMethodCalls = true;
			dbUpdater.doDbTasks((T)this);
			_allowMethodCalls = false;
			_em.flush();
			_tx.commit();
		}
		catch (Exception e) {
			if (_tx.isActive()) {
				_tx.rollback();
			}
			throw e;
		}
		finally {
			_em.close();
		}
	}

	/**
	 * Returns an open EntityManager with which subclasses can perform JPA operations
	 * 
	 * @return open EntityManager
	 * @throws IllegalStateException if this method is called from anywhere other than
	 * inside the doDbTasks method of a subclass of DbWorker
	 */
	protected final EntityManager getEm() {
		if (_allowMethodCalls) {
			return _em;
		}
		throw new IllegalStateException("Calls should only be made within a subclass of DbWorker");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends Identifiable> T findById(Class<T> obj, Long id) {
		return (T)getEm().find(obj, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends Identifiable> T saveObject(T obj) {
		if (obj.getId() == null) {
			getEm().persist(obj);
			return obj;
		}
		return getEm().merge(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends Identifiable> void deleteObject(T obj) {
		EntityManager em = getEm();
		if (obj.getId() == null) {
			throw new IllegalArgumentException("Instance passed cannot be looked up without ID.");
		}
		obj = em.merge(obj);
		em.remove(obj);
	}

}
