package org.conical.common.bbl.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * An implementation of <code>java.sql.Connection</code> that adheres to
 * the general contract for java Connection objects.  For example, the user
 * does not need to do anything more than call close() to release this
 * connection.  This was necessary to support the Connection API while
 * continuing to use the DbConnection heritage class.
 * 
 * @author rdoherty
 */
public class DbConnection implements Connection {

	private DbConnectionBroker _parent;
	private Connection _child;
	
	/**
	 * Creates a DbConnection object given a parent broker and the connection to be wrapped
	 * 
	 * @param parent parent broker
	 * @param child connection to be wrapped
	 */
	public DbConnection(DbConnectionBroker parent, Connection child) {
		_parent = parent;
		_child = child;
	}

	/** {@inheritDoc} */
	@Override
	public void clearWarnings() throws SQLException {
		_child.clearWarnings();
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws SQLException {
		_parent.freeConnection(_child);		
	}

	/** {@inheritDoc} */
	@Override
	public void commit() throws SQLException {
		_child.commit();
	}

	/** {@inheritDoc} */
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return _child.createArrayOf(typeName, elements);
	}

	/** {@inheritDoc} */
	@Override
	public Blob createBlob() throws SQLException {
		return _child.createBlob();
	}

	/** {@inheritDoc} */
	@Override
	public Clob createClob() throws SQLException {
		return _child.createClob();
	}

	/** {@inheritDoc} */
	@Override
	public NClob createNClob() throws SQLException {
		return _child.createNClob();
	}

	/** {@inheritDoc} */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		return _child.createSQLXML();
	}

	/** {@inheritDoc} */
	@Override
	public Statement createStatement() throws SQLException {
		return _child.createStatement();
	}

	/** {@inheritDoc} */
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return _child.createStatement(resultSetType, resultSetConcurrency);
	}

	/** {@inheritDoc} */
	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return _child.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/** {@inheritDoc} */
	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return _child.createStruct(typeName, attributes);
	}

	/** {@inheritDoc} */
	@Override
	public boolean getAutoCommit() throws SQLException {
		return _child.getAutoCommit();
	}

	/** {@inheritDoc} */
	@Override
	public String getCatalog() throws SQLException {
		return _child.getCatalog();
	}

	/** {@inheritDoc} */
	@Override
	public Properties getClientInfo() throws SQLException {
		return _child.getClientInfo();
	}

	/** {@inheritDoc} */
	@Override
	public String getClientInfo(String name) throws SQLException {
		return _child.getClientInfo(name);
	}

	/** {@inheritDoc} */
	@Override
	public int getHoldability() throws SQLException {
		return _child.getHoldability();
	}

	/** {@inheritDoc} */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return _child.getMetaData();
	}

	/** {@inheritDoc} */
	@Override
	public int getTransactionIsolation() throws SQLException {
		return _child.getTransactionIsolation();
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return _child.getTypeMap();
	}

	/** {@inheritDoc} */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return _child.getWarnings();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isClosed() throws SQLException {
		return _child.isClosed();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReadOnly() throws SQLException {
		return _child.isReadOnly();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValid(int timeout) throws SQLException {
		return _child.isValid(timeout);
	}

	/** {@inheritDoc} */
	@Override
	public String nativeSQL(String sql) throws SQLException {
		return _child.nativeSQL(sql);
	}

	/** {@inheritDoc} */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return _child.prepareCall(sql);
	}

	/** {@inheritDoc} */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return _child.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	/** {@inheritDoc} */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return _child.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return _child.prepareStatement(sql);
	}

	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return _child.prepareStatement(sql, autoGeneratedKeys);
	}

	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return _child.prepareStatement(sql, columnIndexes);
	}
	
	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return _child.prepareStatement(sql, columnNames);
	}

	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return _child.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	/** {@inheritDoc} */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return _child.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/** {@inheritDoc} */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		_child.releaseSavepoint(savepoint);		
	}

	/** {@inheritDoc} */
	@Override
	public void rollback() throws SQLException {
		_child.rollback();
	}

	/** {@inheritDoc} */
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		_child.rollback(savepoint);
	}

	/** {@inheritDoc} */
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		_child.setAutoCommit(autoCommit);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCatalog(String catalog) throws SQLException {
		_child.setCatalog(catalog);
	}

	/** {@inheritDoc} */
	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		_child.setClientInfo(properties);
	}

	/** {@inheritDoc} */
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		_child.setClientInfo(name, value);
	}

	/** {@inheritDoc} */
	@Override
	public void setHoldability(int holdability) throws SQLException {
		_child.setHoldability(holdability);
	}

	/** {@inheritDoc} */
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		_child.setReadOnly(readOnly);
	}

	/** {@inheritDoc} */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		return _child.setSavepoint();
	}

	/** {@inheritDoc} */
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return _child.setSavepoint(name);
	}

	/** {@inheritDoc} */
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		_child.setTransactionIsolation(level);
	}

	/** {@inheritDoc} */
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		_child.setTypeMap(map);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return _child.isWrapperFor(iface);
	}

	/** {@inheritDoc} */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return _child.unwrap(iface);
	}

	/** {@inheritDoc} */
	@Override
	public void setSchema(String schema) throws SQLException {
		_child.setSchema(schema);
	}

	/** {@inheritDoc} */
	@Override
	public String getSchema() throws SQLException {
		return _child.getSchema();
	}

	/** {@inheritDoc} */
	@Override
	public void abort(Executor executor) throws SQLException {
		_child.abort(executor);		
	}

	/** {@inheritDoc} */
	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		_child.setNetworkTimeout(executor, milliseconds);
	}

	/** {@inheritDoc} */
	@Override
	public int getNetworkTimeout() throws SQLException {
		return _child.getNetworkTimeout();
	}
}
