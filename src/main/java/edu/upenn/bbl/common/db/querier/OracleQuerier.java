package edu.upenn.bbl.common.db.querier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Implementation of Querier for Oracle databases.
 * 
 * @author rdoherty
 */
public class OracleQuerier extends Querier {

	private enum OracleType {
		CHAR(DataType.STRING),
		VARCHAR(DataType.STRING),
		VARCHAR2(DataType.STRING),
		CLOB(DataType.STRING),
		LONG(DataType.STRING),
		NUMBER(DataType.FLOAT),
		DATE(DataType.DATE),
		TIMESTAMP(DataType.DATE),
		BLOB(DataType.BINARY),
		RAW(DataType.BINARY),
		ROWID(DataType.INTEGER),
		UNDEFINED(DataType.UNDEFINED);
		
		private DataType _codebookType;
		
		private OracleType(DataType codebookType) {
			_codebookType = codebookType;
		}
		
		public DataType getCodebookType() {
			return _codebookType;
		}
	}
	
	private static final String TABLE_SELECT_SQL = "select tname from tab where tabtype='TABLE' order by tname";
	private static final String COLUMN_SELECT_SQL = "select cname, nulls, coltype, width, colno from col where tname = ? order by colno";
	private static final Object ORACLE_NOT_NULL = "NOT NULL";
	
	/**
	 * Creates a Oracle-specific Querier using the passed data source
	 * 
	 * @param dbInfo
	 */
	public OracleQuerier(DataSourceReference dbInfo) {
		super(dbInfo);
	}

	@Override
	protected String getTableSelectionSql() {
		return TABLE_SELECT_SQL;
	}
	
	@Override
	protected ColumnModel getColumnModelFromResult(ResultSet rs) throws SQLException {
		ColumnModel col = new ColumnModel();
		col.name = rs.getString(1);
		col.nullAllowed = !rs.getString(2).equals(ORACLE_NOT_NULL);
		col.type = OracleType.valueOf(rs.getString(3)).getCodebookType();
		col.size1 = rs.getInt(4);
		col.colnum = rs.getInt(5);
		return col;
	}

	@Override
	protected PreparedStatement getPsForColumnLookup(Connection conn, String tableName) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(COLUMN_SELECT_SQL);
		ps.setString(1, tableName.toUpperCase());
		return ps;
	}
	
	@Override
	protected void assignKeyInformation(Set<ColumnModel> columnSet, String tableName) {
		// TODO: write; for now Oracle will not have key info
	}
}
