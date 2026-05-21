package edu.upenn.bbl.common.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates user attributes.
 * 
 * @author rdoherty
 */
public class User {

	private Long _id;
	private String _username;
	private String _firstName;
	private String _lastName;
	private String _title;
	private String _emailAddress;
	private Set<String> _accessRoles = new HashSet<String>();
	
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		_username = username;
	}
	
	public String getFirstName() {
		return _firstName;
	}
	public void setFirstName(String firstName) {
		_firstName = firstName;
	}
	
	public String getLastName() {
		return _lastName;
	}
	public void setLastName(String lastName) {
		_lastName = lastName;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		_title = title;
	}
	
	public String getEmailAddress() {
		return _emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	/**
	 * Returns set of access roles currently given to this user
	 * 
	 * @return set of roles
	 */
	public Set<String> getAccessRoles() {
		return _accessRoles;
	}
 	public void setAccessRoles(Set<String> accessRoles) {
		_accessRoles = accessRoles;
	}
	
	/**
	 * Returns true if passed role is among this user's roles, else false
	 * 
	 * @param accessRole access role to test for
	 * @return true if user is in role, else false
	 */
	public boolean hasAccessRole(String accessRole) {
		return _accessRoles.contains(accessRole);
	}
	
	/**
	 * Adds single access role to this user
	 * 
	 * @param accessRole role to add
	 */
	public void addAccessRole(String accessRole) {
		_accessRoles.add(accessRole);
	}
	
	/**
	 * Adds multiple access roles to this user at one time
	 * 
	 * @param accessRoles roles to add
	 */
	public void addAccessRoles(Collection<String> accessRoles) {
		_accessRoles.addAll(accessRoles);
	}
	
}
