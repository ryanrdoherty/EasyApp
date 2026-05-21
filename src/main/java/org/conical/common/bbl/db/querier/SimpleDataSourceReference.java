package org.conical.common.bbl.db.querier;

import javax.sql.DataSource;

import org.conical.common.bbl.db.DataSourceConfig;
import org.conical.common.bbl.db.DataSourceFactory;
import org.conical.common.bbl.db.DatabaseType;

/**
 * Simple implementation of <code>DataSourceReference</code> which uses a
 * <code>DataSourceConfig</code> to access a specific database via the
 * <code>DataSourceFactory</code>.  For use by users who simply want to
 * access the capabilities of the <code>Querier</code> without a custom
 * implementation to configure their <code>DataSource</code> or look up
 * via JNDI.
 * 
 * @author rdoherty
 */
public class SimpleDataSourceReference implements DataSourceReference {

	private DatabaseType _dbType;
	private DataSource _ds;

	/**
	 * Constructor creates an instance configured by the passed <code>DataSourceConfig</code>.
	 * 
	 * @param config database configuration to access
	 */
	public SimpleDataSourceReference(DataSourceConfig config) {
		_dbType = config.getDbType();
		_ds = DataSourceFactory.getDataSource(config);
	}
	
	/**
	 * @return the type of database this DataSourceInfo is configured to help access
	 */
	@Override
	public DatabaseType getType() {
		return _dbType;
	}

	/**
	 * @return a reference to the data source configured
	 */
	@Override
	public DataSource lookUpDataSource() {
		return _ds;
	}

}
