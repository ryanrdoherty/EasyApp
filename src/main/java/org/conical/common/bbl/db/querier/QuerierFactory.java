package org.conical.common.bbl.db.querier;

/**
 * Factory class that enables construction of a Querier tailored to
 * a specific database type and ready to access a specific data source.
 * Typical usage might be:
 * <code>
 *   String bundleName = "databaseProps";
 *   DataSourceConfig config = new DataSourceConfig(bundleName);
 *   DataSourceReference dsRef = new SimpleDataSourceReference(config);
 *   Querier querier = QuerierFactory.getQuerier(dsRef);
 *   ...
 *   // querier is ready to use
 * </code>
 * 
 * @author rdoherty
 */
public class QuerierFactory {

	/**
	 * Generates and returns a Querier specific to the database type and
	 * ready to access the DataSource contained in the reference class passed.
	 * 
	 * @param dsRef reference to type of and path to database
	 * @return tailored Querier
	 */
	public static Querier getQuerier(DataSourceReference dsRef) {
		switch (dsRef.getType()) {
		    case ORACLE: return new OracleQuerier(dsRef);
		    case MYSQL: return new MySqlQuerier(dsRef);
		    default:
		    	throw new UnsupportedOperationException(
		    		"SourceType " + dsRef.getType() + " is not currently supported.");
		}
	}

}
