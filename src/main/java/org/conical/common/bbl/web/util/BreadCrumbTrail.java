package org.conical.common.bbl.web.util;

import java.util.Collection;
import java.util.Stack;

/**
 * Keeps track of BreadCrumbs and manages how they are added and removed.  This implementation
 * uses a stack to access the crumbs, and will remove crumbs that are the same level deep as
 * others are added.
 * 
 * @author rdoherty
 *
 */
public class BreadCrumbTrail {
	
	private Stack<BreadCrumb> _stack = new Stack<BreadCrumb>();
	
	/**
	 * Adds the passed <code>BreadCrumb</code> to the trail.  If the trail is empty,
	 * simply pushes onto the stack.  If not, traverses the stack looking for a BreadCrumb
	 * of a lower level, removing each as it goes along.  If it finds one of the same
	 * level, that one will be the last to be removed and the passed crumb replaces it.
	 * If the stack is empty before this happens, the passed crumb is simply added and
	 * is the lowest level.
	 * 
	 * @param newCrumb new crumb to be added
	 */
	public void addBreadCrumb(BreadCrumb newCrumb) {
		while (!_stack.isEmpty() && _stack.peek().getLevel() >= newCrumb.getLevel()) {
			_stack.pop();
		}
		_stack.push(newCrumb);
	}

	/**
	 * Returns the current stack of crumbs (in bottom to top order).
	 * 
	 * @return current bread crumbs on the stack
	 */
	public Collection<BreadCrumb> getCrumbs() {
		return _stack;
	}

	/**
	 * Clears the stack of crumbs.
	 */
	public void clear() {
		_stack.clear();
	}
}
