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
	private Set<AccessRole> _accessRoles = new HashSet<AccessRole>();
	
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
	
	public Set<AccessRole> getAccessRoles() {
		return _accessRoles;
	}
	public void setAccessRoles(Set<AccessRole> accessRoles) {
		_accessRoles = accessRoles;
	}
	public void addAccessRole(AccessRole accessRole) {
		_accessRoles.add(accessRole);
	}
	public void addAccessRoles(Collection<AccessRole> accessRoles) {
		_accessRoles.addAll(accessRoles);
	}
	
}
