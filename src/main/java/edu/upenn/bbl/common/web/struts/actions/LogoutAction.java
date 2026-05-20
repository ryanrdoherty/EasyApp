package edu.upenn.bbl.common.web.struts.actions;

import org.apache.struts2.dispatcher.SessionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionSupport;

/**
 * Logs user out of the application by invalidating the entire session
 * 
 * @author rdoherty
 */
public class LogoutAction extends ActionSupport {

  private static final long serialVersionUID = 20100528L;

  private static Logger LOG = LoggerFactory.getLogger(LogoutAction.class);

  /**
   * Logs user out of the application by invalidating the entire session
   * 
   * @return success
   */
  @Override
  public String execute() throws Exception {
    SessionMap session = (SessionMap)ActionContext.getContext().getSession();
    try {
      session.invalidate();
    }
    catch (IllegalStateException ise) {
      LOG.warn("Tried to invalidate an already invalidated session.", ise);
    }
    return SUCCESS;
  }

}
