package edu.upenn.bbl.common.web.struts.actions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import edu.upenn.bbl.common.auth.User;
import edu.upenn.bbl.common.enums.Ethnicity;
import edu.upenn.bbl.common.enums.Handedness;
import edu.upenn.bbl.common.enums.NameSuffix;
import edu.upenn.bbl.common.enums.Race;
import edu.upenn.bbl.common.enums.Religion;
import edu.upenn.bbl.common.enums.Sex;
import edu.upenn.bbl.common.enums.SexualOrientation;
import edu.upenn.bbl.common.enums.State;
import edu.upenn.bbl.common.util.PropertyMapLoader;

/**
 * This should be the base class for all actions in Struts2-based
 * BBL web apps.  It takes care of a number of things:
 * <ul>
 *   <li>loads application/developer attributes from properties and exposing them</li>
 *   <li>PERMISSION_DENIED return value</li>
 *   <li>does user authentication, denying access if desired and allowing overrides for specific roles</li>
 *   <li>exposes access to the User object representing the current user</li>
 *   <li>exposes access to attributes needed by the standard BBL JSP header</li>
 *   <li>exposes access to common enums</li>
 * </ul>
 * 
 * @author rdoherty
 */
public abstract class BaseAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 20100526L;
	
	static final String USER_KEY = "user_key";
	
	/** Should be returned when permission to view a page or perform an action is denied */
	protected static final String PERMISSION_DENIED = "permissionDenied";

	private static final String APP_CONFIG_BUNDLE = "application";
	private static final String APP_NAME_KEY = "application.name";
	
	private static Map<String, String> _propertyMap = PropertyMapLoader.loadProperties(APP_CONFIG_BUNDLE);

	/**
	 * Encapsulates information about the contact developer for this application
	 * 
	 * @author rdoherty
	 */
	public class Developer {
		public String getName() { return _propertyMap.get("developer.name"); }
		public String getTitle() { return _propertyMap.get("developer.title"); }
		public String getPhone() { return _propertyMap.get("developer.phone"); }
		public String getEmail() { return _propertyMap.get("developer.email"); }
	}
	
	private String _originalRequestUrl;
	
	/**
	 * Returns whether this action should require a logged in user
	 * 
	 * @return true if action requires user to be logged in, else false
	 */
	protected abstract boolean actionRequiresLogin();
	
	/**
	 * Child classes must implement.  This is the "work" of the action and will be
	 * called by BaseAction.execute().
	 * 
	 * @return action status
	 * @throws Exception if something goes wrong
	 */
	protected abstract String doWork() throws Exception;

	/**
	 * Method called by Struts to initiate action logic.  Takes care of user
	 * authentication via actionRequiresLogin() and access roles.  Simply
	 * returns the value returned by doWork();
	 * 
	 * @return action status
	 * @throws Exception if something goes wrong
	 */
	public final String execute() throws Exception {
		User user = getCurrentUser();
		if (user == null && actionRequiresLogin()) {
			return LOGIN;
		}
		if (user != null) {
			for (String requiredRole : getRequiredAccessRoles()) {
				if (!user.getAccessRoles().contains(requiredRole)) {
					return PERMISSION_DENIED;
				}
			}
		}
		return doWork();
	}

	/**
	 * Returns the user currently logged into this application, or null if
	 * no one is logged in.
	 * 
	 * @return current user
	 */
	public final User getCurrentUser() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		return (User)session.get(USER_KEY);
	}

	/**
	 * Exposes session to child classes.
	 * 
	 * @return map of session objects
	 */
	protected Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}
	
	/**
	 * Returns the set of access roles required to execute this action (and
	 * access the associated page).  Base class returns an empty set.  Child
	 * classes are encouraged to override, returning application-specific roles.
	 * 
	 * @return set of roles required to view this page
	 */
	protected Set<String> getRequiredAccessRoles() {
		return new HashSet<String>();
	}
	
	/**
	 * Returns the original URL for this request.  Useful to redirect to
	 * this page, say, after log in
	 * 
	 * @return original URL
	 */
	public String getRequestUrl() {
		return _originalRequestUrl;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setServletRequest(HttpServletRequest request) {
		_originalRequestUrl = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			_originalRequestUrl += "?" + request.getQueryString();
		}
		LOG.debug("Have set request url to: " + _originalRequestUrl);
	}

	/**
	 * Returns a map of application properties read from the application.properties
	 * file.  This allows applications to add their own custom properties to the file
	 * and read them at will from actions.
	 * 
	 * @return map of key-value pairs read from application.properties
	 */
	protected Map<String, String> getApplicationProperties() {
		return _propertyMap;
	}

	/**
	 * Returns the name of this application
	 * 
	 * @return application name
	 */
	public String getApplicationName() {
		return _propertyMap.get(APP_NAME_KEY);
	}
	
	/**
	 * Returns the developer who should be contacted regarding this application
	 * 
	 * @return application developer
	 */
	public Developer getDeveloper() {
		return new Developer();
	}

	/**
	 * @return List of all States
	 */
	public List<State> getStates() {
		return Arrays.asList(State.values());
	}

	/**
	 * @return List of all Sexes
	 */
	public List<Sex> getSexes() {
		return Arrays.asList(Sex.values());
	}

	/**
	 * @return List of all Races
	 */
	public List<Race> getRaces() {
		return Arrays.asList(Race.values());
	}

	/**
	 * @return List of all Ethnicities
	 */
	public List<Ethnicity> getEthnicities() {
		return Arrays.asList(Ethnicity.values());
	}

	/**
	 * @return List of all Handednesses
	 */
	public List<Handedness> getHandednesses() {
		return Arrays.asList(Handedness.values());
	}
	
	/**
	 * @return List of all NameSuffixes
	 */
	public List<NameSuffix> getNameSuffixes() {
		return Arrays.asList(NameSuffix.values());
	}
	
	/**
	 * @return List of all SexualOrientations
	 */
	public List<SexualOrientation> getSexualOrientations() {
		return Arrays.asList(SexualOrientation.values());
	}
	
	/**
	 * @return List of all Religions
	 */
	public List<Religion> getReligions() {
		return Arrays.asList(Religion.values());
	}
}
