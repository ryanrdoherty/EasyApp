package org.conical.common.bbl.web.struts.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.ServletRequestAware;
import org.apache.struts2.action.ServletResponseAware;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Logs user out of the application by invalidating the entire session
 * 
 * @author rdoherty
 */
public class LogoutAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

  private static final Logger LOG = LogManager.getLogger(LogoutAction.class);

  private static final long serialVersionUID = 20100528L;

  private HttpServletRequest _request;
  private HttpServletResponse _response;

  /**
   * Logs user out of the application by invalidating the entire session
   * 
   * @return success
   */
  @Override
  public String execute() throws Exception {
    try {
      _request.getSession().invalidate();
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
   * Returns additional cookies to send to user agent upon logout. Default is to return an empty list; can be
   * overridden to return a custom cookie list.
   * 
   * @return list of custom cookies to return upon logout
   */
  protected List<Cookie> getAdditionalCookies() {
    return new ArrayList<Cookie>();
  }

  @Override
  public void withServletResponse(HttpServletResponse response) {
    _response = response;
  }

  @Override
  public void withServletRequest(HttpServletRequest request) {
    _request = request;
  }

}
