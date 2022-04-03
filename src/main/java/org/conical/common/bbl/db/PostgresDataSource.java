package org.conical.common.bbl.db;

import org.conical.common.bbl.exception.BBLRuntimeException;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * An extension of PGSimpleDataSource that allows caller to assign
 * settings via a postgresql connection URL, rather than specifying
 * the server name, port and database name separately.
 * 
 * @author rdoherty
 */
public class PostgresDataSource extends PGSimpleDataSource {
	
	private static final long serialVersionUID = 20100513L;
	
	public PostgresDataSource() {
		super();
	}
	
	/**
	 * Assigns a postgres URL to this data source.
	 * 
	 * @param url
	 */
	public void setURL(String url) {
		try {
			String[] args = DatabaseType.POSTGRES.getUrlParts(url);
			setServerNames(new String[] { args[0] });
			setPortNumbers(new int[] { Integer.parseInt(args[1]) });
			setDatabaseName(args[2]);
		}
		catch (Exception e) {
			throw new BBLRuntimeException("Unable to parse URL: " + url, e);
		}
	}
}
