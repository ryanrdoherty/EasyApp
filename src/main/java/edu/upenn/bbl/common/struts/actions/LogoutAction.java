package edu.upenn.bbl.common.struts.actions;

import org.apache.struts2.dispatcher.SessionMap;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Logs user out of the application by invalidating the entire session
 * 
 * @author rdoherty
 */
public class LogoutAction extends ActionSupport {

	private static final long serialVersionUID = 20100528L;

	/**
	 * Logs user out of the application by invalidating the entire session
	 * 
	 * @return success
	 */
	public String execute() throws Exception {
		SessionMap<String, Object> session = (SessionMap<String, Object>)ActionContext.getContext().getSession();
		try {
			session.invalidate();
		}
		catch (IllegalStateException ise) {
			LOG.warn("Tried to invalidate an already invalidated session.", ise);
		}
		return SUCCESS;
	}
	
}
