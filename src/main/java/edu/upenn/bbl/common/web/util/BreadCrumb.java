package edu.upenn.bbl.common.web.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Breadcrumbs are used to navigate webpages.  This implementation uses levels
 * to indicate how deep in the tree this breadcrumb is.  <code>BreadCrumbTrail</code>
 * then interprets this level and adds/removes breadcrumbs based on each one added.
 * Also available are the link the breadcrumb is a reference to and the display
 * value that should be shown.
 * 
 * @author rdoherty
 */
public class BreadCrumb {

	private String _link;
	private String _displayValue;
	private int _level;
	
	/**
	 * Constructor which takes all information contained in this instance.
	 * 
	 * @param link link that should be navigated to when this breadcrumb is clicked
	 * @param displayValue value displayed on button or link
	 * @param level depth in the link tree
	 */
	public BreadCrumb(String link, String displayValue, int level) {
		_link = link;
		_displayValue = displayValue;
		_level = level;
	}
	
	public String getLink() {
		return _link;
	}
	public void setLink(String link) {
		_link = link;
	}
	
	public String getDisplayValue() {
		return _displayValue;
	}
	public void setDisplayValue(String displayValue) {
		_displayValue = displayValue;
	}
	
	public int getLevel() {
		return _level;
	}
	public void setLevel(int level) {
		_level = level;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_FIELD_NAMES_STYLE);
	}
	
}
