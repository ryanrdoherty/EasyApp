package org.conical.common.bbl.web.struts.actions;

/**
 * This class exists as a simple child of BaseAction to show how little
 * code must be written to implement an action.  It can be used as a
 * model for those learning Struts and the BBL Struts framework.
 * 
 * @author rdoherty
 */
public class SampleAction extends BaseAction {

	private static final long serialVersionUID = 20100708L;

	/**
	 * Returns true since most applications require login.
	 * 
	 * @return true
	 */
	@Override
	protected boolean actionRequiresLogin() {
		return true;
	}

	/**
	 * Performs no side effects; simply returns success.
	 * 
	 * @return success
	 */
	@Override
	protected String doWork() throws Exception {
		return SUCCESS;
	}

}
