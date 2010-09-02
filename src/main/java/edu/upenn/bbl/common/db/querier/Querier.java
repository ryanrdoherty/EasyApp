package edu.upenn.bbl.common.db.querier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upenn.bbl.common.db.DbUtil;

/**
 * Abstract class which provides an interface allowing callers to look up table
 * and column names in a database.  Children should implement the parts of this
 * class that are vendor-specific.
 * 
 * @author rdoherty
 */
public abstract class Querier {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(Querier.class.getName());
	
	private DataSourceReference _dbInfo;
	
	protected Querier(DataSourceReference dbInfo) {
		_dbInfo = dbInfo;
	}
	
	protected abstract String getTableSelectionSql();
	protected abstract PreparedStatement getPsForColumnLookup(Connection conn, String tableName) throws SQLException;
	protected abstract ColumnModel getColumnModelFromResult(ResultSet rs) throws SQLException;
	
	protected void assignKeyInformation(Set<ColumnModel> columnSet, String tableName) {
		// default implementation does nothing; children may override
	}
	
	protected Connection getConnection() throws SQLException {
		return _dbInfo.lookUpDataSource().getConnection();
	}
	
	/**
	 * Retrieves a set of all readable table names in the database
	 * this Querier was configured for
	 * 
	 * @return a set of table names
	 */
	public Set<String> getTableNames() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<String> tableSet = new LinkedHashSet<String>();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(getTableSelectionSql());
			rs = ps.executeQuery();
			while (rs.next()) {
				tableSet.add(rs.getString(1));
			}
			return tableSet;
		}
		catch (SQLException sqle) {
			throw new RuntimeException("Unable to query database.", sqle);
		}
		finally {
			DbUtil.close(rs, ps, conn);
		}
	}

	/**
	 * Retrieves a set of all the columns in the table with the given name
	 * 
	 * @param tableName name for which to retrieve columns
	 * @return set of columns
	 */
	public Set<ColumnModel> getColumnsFromTable(String tableName) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Set<ColumnModel> columnSet = new LinkedHashSet<ColumnModel>();
		try {
			conn = getConnection();
			ps = getPsForColumnLookup(conn, tableName);
			ps.setString(1, tableName);
			rs = ps.executeQuery();
			while(rs.next()) {
				columnSet.add(getColumnModelFromResult(rs));
			}
			assignKeyInformation(columnSet, tableName);
			return columnSet;
		}
		catch (SQLException sqle) {
			throw new RuntimeException("Unable to query database.", sqle);
		}
		finally {
			DbUtil.close(rs, ps, conn);
		}
	}
}
