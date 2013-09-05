package edu.upenn.bbl.common.web.struts.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Logs user out of the application by invalidating the entire session
 * 
 * @author rdoherty
 */
public class LogoutAction extends ActionSupport implements ServletResponseAware {

	private static final long serialVersionUID = 20100528L;

	private HttpServletResponse _response;
	
	/**
	 * Logs user out of the application by invalidating the entire session
	 * 
	 * @return success
	 */
	@Override
	public String execute() throws Exception {
		SessionMap<String, Object> session = (SessionMap<String, Object>)ActionContext.getContext().getSession();
		try {
			session.invalidate();
			for (Cookie cookie : getAdditionalCookies()) {
			  _response.addCookie(cookie);
			}
		}
		catch (IllegalStateException ise) {
			LOG.warn("Tried to invalidate an already invalidated session.", ise);
		}
		return SUCCESS;
	}

	/**
	 * Returns additional cookies to send to user agent upon logout.  Default is
	 * to return an empty list; can be overridden to return a custom cookie list.
	 * 
	 * @return list of custom cookies to return upon logout
	 */
	protected List<Cookie> getAdditionalCookies() {
		return new ArrayList<Cookie>();
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		_response = response;
	}
	
}
