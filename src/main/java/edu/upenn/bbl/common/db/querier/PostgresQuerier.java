package edu.upenn.bbl.common.db.querier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Implementation of Querier for Postgres databases.
 * 
 * NOTE: this class is not implemented and does not work!
 * 
 * TODO: implement
 * 
 * @author rdoherty
 */
public class PostgresQuerier extends Querier {

	/**
	 * Creates a Postgres-specific Querier using the passed data source
	 * 
	 * @param source reference to data source for this querier
	 */
	public PostgresQuerier(DataSourceReference source) {
		super(source);
	}

	@Override
	protected ColumnModel getColumnModelFromResult(ResultSet rs)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PreparedStatement getPsForColumnLookup(Connection conn,
			String tableName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableSelectionSql() {
		// TODO Auto-generated method stub
		return null;
	}


}
