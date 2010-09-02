package edu.upenn.bbl.common.db.querier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of Querier for MySQL databases.
 * 
 * @author rdoherty
 */
public class MySqlQuerier extends Querier {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(MySqlQuerier.class.getName());
	
	private enum MySqlType {
		BIGINT    (DataType.INTEGER),
		CHAR      (DataType.STRING),
		DATE      (DataType.DATE),
		DATETIME  (DataType.DATE),
		DECIMAL   (DataType.FLOAT),
		DOUBLE    (DataType.FLOAT),
		ENUM      (DataType.ENUM),
		FLOAT     (DataType.FLOAT),
		INT       (DataType.INTEGER),
		LONGTEXT  (DataType.STRING),
		SMALLINT  (DataType.INTEGER),
		TEXT      (DataType.STRING),
		TIME      (DataType.DATE),
		TIMESTAMP (DataType.DATE),
		TINYINT   (DataType.INTEGER),
		VARCHAR   (DataType.STRING);
		
		private DataType _codebookType;
		
		private MySqlType(DataType codebookType) {
			_codebookType = codebookType;
		}
		
		public DataType getCodebookType() {
			return _codebookType;
		}		
	}
	
	private static final String TABLE_SELECT_SQL =
		"select table_name from information_schema.tables " +
		"where table_type = 'BASE TABLE' and table_schema != 'test'";

	private static final String COLUMN_SELECT_SQL =
		"select column_name," +               // 1
		"       ordinal_position," +          // 2
		"       column_key," +                // 3
		"       is_nullable," +               // 4
		"       data_type," +                 // 5
		"       character_maximum_length, " + // 6
		"       numeric_precision," +         // 7
		"       numeric_scale," +             // 8
		"       column_type " +               // 9
		"from information_schema.columns " +
		"where table_name = ? " +
		"order by ordinal_position";

	private static final Object MYSQL_PRIMARY_KEY = "PRI";
	private static final Object MYSQL_IS_NULLABLE_VAL = "YES";

	/**
	 * Creates a MySql-specific Querier using the passed data source
	 * 
	 * @param dbInfo
	 */
	public MySqlQuerier(DataSourceReference dbInfo) {
		super(dbInfo);
	}
	
	@Override
	protected String getTableSelectionSql() {
		return TABLE_SELECT_SQL;
	}
	
	@Override
	protected PreparedStatement getPsForColumnLookup(Connection conn, String tableName) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(COLUMN_SELECT_SQL);
		ps.setString(1, tableName);
		return ps;
	}
	
	@Override
	protected ColumnModel getColumnModelFromResult(ResultSet rs) throws SQLException {
		ColumnModel col = new ColumnModel();
		col.name = rs.getString(1);
		col.colnum = rs.getInt(2);
		col.isKey = MYSQL_PRIMARY_KEY.equals(rs.getString(3));
		col.nullAllowed = MYSQL_IS_NULLABLE_VAL.equals(rs.getString(4));
		col.type = MySqlType.valueOf(rs.getString(5).toUpperCase()).getCodebookType();
		switch(col.type) {
			case STRING:
			case ENUM:
				col.size1 = rs.getInt(6);
				break;
			case FLOAT:
			case INTEGER:
				col.size1 = rs.getInt(7);
				col.size2 = rs.getInt(8);
				break;
			default:
				// do nothing with the other data types
		}
		// interpret MySql's enum type value list
		if (DataType.ENUM.equals(col.type)) {
			String enumValRange = rs.getString(9);
			String[] vals = StringUtils.split(enumValRange.substring(5, enumValRange.length()-1),",");
			List<String> outputList = new ArrayList<String>();
			for (String val : vals) {
				outputList.add(val.substring(1, val.length()-1));
			}
			col.range = StringUtils.join(outputList, ",");
		}
		return col;
	}
}
