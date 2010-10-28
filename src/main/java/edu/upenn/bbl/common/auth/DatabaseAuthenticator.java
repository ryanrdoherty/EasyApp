package edu.upenn.bbl.common.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upenn.bbl.common.db.DataSourceFactory;
import edu.upenn.bbl.common.db.DbUtil;
import edu.upenn.bbl.common.exception.BBLRuntimeException;

/**
 * Authenticates users and assigns access roles based on custom database
 * tables.  For now, that is:
 * 
 * <ul>
 *   <li>Accesses the USERS table in Oracle for user attributes and password checking</li>
 *   <li>Assigns COORDINATOR role via the COORDINATOR column in that table</li>
 *   <li>Assigns all other roles via the USER_ROLES table</li>
 * </ul>
 * 
 * @author rdoherty
 */
public class DatabaseAuthenticator implements Authenticator {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(DatabaseAuthenticator.class.getName());
	
	private static final String GET_UNAUTH_USER_ATTRIBUTES =
		"select user_name, fname, lname, coordinator from users where user_name = ?";

	private static final String GET_AUTH_USER_ATTRIBUTES =
		GET_UNAUTH_USER_ATTRIBUTES + " and user_pass = ?";

	private static final String GET_ROLES_BY_USER_ID =
		"select role_name from user_roles where user_name = ?";

	private DataSource _ds;

	/**
	 * Creates a new instance with the given config
	 * 
	 * @param config configuration (must be a database configuration)
	 * @throws AuthenticationException if error occurs creating the Authenticator
	 */
	public DatabaseAuthenticator(AuthConfig config) throws AuthenticationException {
		_ds = AuthDataSource.getInstance(config).getDataSource();
	}

	/**
	 * Creates a new instance using a DataSource configured in JNDI.  Looks up
	 * the resource using the passed name.
	 * 
	 * @param jndiName JNDI name the desired data source is registered under
	 * @throws AuthenticationException if unable to look up data source
	 */
	public DatabaseAuthenticator(String jndiName) throws AuthenticationException {
		try {
			_ds = DataSourceFactory.getJndiDataSource(jndiName);
		}
		catch (BBLRuntimeException e) {
			throw new AuthenticationException("Unable to look up Authentication DataSource", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getAuthenticatedUser(String username, String password) throws AuthenticationException {
		return getUser(username, password, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUnauthenticatedUser(String username) throws AuthenticationException {
		return getUser(username, null, false);
	}
	
	private User getUser(String username, String password, boolean authenticate) throws AuthenticationException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = _ds.getConnection();
			String query = (authenticate ? GET_AUTH_USER_ATTRIBUTES : GET_UNAUTH_USER_ATTRIBUTES);
			ps = conn.prepareStatement(query);
		    ps.setString(1, username);
			if (authenticate) {
				ps.setString(2, password);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				User u = new User();
				u.setUsername(rs.getString(1));
				u.setFirstName(rs.getString(2));
				u.setLastName(rs.getString(3));
				String coord = rs.getString(4);
				if (coord != null && coord.equals("Y")) {
					u.addAccessRole("COORDINATOR");
				}
				u.addAccessRoles(getAccessRoles(u));
				return u;
			}
			return null;
		}
		catch (SQLException sqle) {
			throw new AuthenticationException("Could not verify user with database", sqle);
		}
		finally {
			DbUtil.close(rs, ps, conn);
		}
	}

	private Set<String> getAccessRoles(User user) throws AuthenticationException {
		Set<String> list = new HashSet<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = _ds.getConnection();
			ps = conn.prepareStatement(GET_ROLES_BY_USER_ID);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		}
		catch (SQLException sqle) {
			throw new AuthenticationException("Unable to look up access roles for user "  + user.getUsername(), sqle);
		}
		finally {
			DbUtil.close(rs, ps, conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidUser(String username, String password)
			throws AuthenticationException {
		User u = getAuthenticatedUser(username, password);
		return (u != null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean userHasRole(String username, String role)
			throws AuthenticationException {
		User u = getUnauthenticatedUser(username);
		if (u == null) {
			return false;
		}
		return (u.getAccessRoles().contains(role));
	}
}
