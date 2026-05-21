package edu.upenn.bbl.common.db.querier;

import javax.sql.DataSource;

import edu.upenn.bbl.common.db.DatabaseType;

/**
 * Encapsulates both a type/brand of database and a DataSource
 * representing an instance of that type.
 * 
 * @author rdoherty
 *
 */
public interface DataSourceReference {

	/**
	 * @return type of database this info is for
	 */
	public DatabaseType getType();
	
	/**
	 * @return DataSource object associated with a give type of database
	 */
	public DataSource lookUpDataSource();

}
