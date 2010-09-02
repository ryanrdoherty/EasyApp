package edu.upenn.bbl.common.struts.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upenn.bbl.common.util.PropertyMapLoader;

/**
 * This action should be referred to when a user help button or link is clicked.  It takes
 * one parameter, "page", which is the page for which help is desired.  The action looks
 * this page up in the help.properties file to find the resource containing helpful
 * information about that specific page.  If no mapping or no page exists, the action
 * provides sensible text.
 * 
 * @author rdoherty
 */
public class HelpAction extends BaseAction {

	private static final long serialVersionUID = 20100504L;

	private static Logger LOG = LoggerFactory.getLogger(HelpAction.class.getName());
	
	private static final String HELP_CONFIG_BUNDLE = "help";

	private static final String DEFAULT_HELP_TEXT = "No help page exists for this page ({0}).";
	private static final String NOT_FOUND_HELP_TEXT = "Help page for page {0} ({1}) cannot be found.";
	
	private static Map<String, String> _helpPageMap = PropertyMapLoader.loadProperties(HELP_CONFIG_BUNDLE);
	
	private String _referringPage;
	private String _helpPageText;
	
	/**
	 * For security purposes, help pages are access-protected.
	 * 
	 * @return true
	 */
	@Override
	protected boolean actionRequiresLogin() {
		return true;
	}

	/**
	 * Processes page argument and attempts to find help text for the given page.  Sets helpPageText
	 * accordingly.
	 * 
	 * @return success (should always provide some sort of text even if parameter is missing or bad)
	 * @throws Exception if IO or other error occurs
	 */
	@Override
	protected String doWork() throws Exception {
		if (StringUtils.isEmpty(_referringPage)) {
			_helpPageText = MessageFormat.format(DEFAULT_HELP_TEXT, _referringPage);
			return SUCCESS;
		}
		URL url = new URL(_referringPage);
		String page = url.getPath();
		// remove context root to get path within application
		page = page.substring(page.indexOf('/', 1) + 1, page.length());
		_helpPageText = generateHelpPageText(page);
		return SUCCESS;
	}
	
	private String generateHelpPageText(String page) {
		String helpPage = _helpPageMap.get(page);
		if (helpPage == null) {
			return MessageFormat.format(DEFAULT_HELP_TEXT, page);
		}
		BufferedReader reader = null;
		String text = "";
		String NL = System.getProperty("line.separator");
		try {
			InputStream in = HelpAction.class.getResourceAsStream(helpPage);
			if (in == null) {
				LOG.error("Misconfiguration: unable to locate help page resource at " + helpPage);
				return MessageFormat.format(NOT_FOUND_HELP_TEXT, page, helpPage);
			}
			reader = new BufferedReader(new InputStreamReader(in));
			while (reader.ready()) {
				text += reader.readLine() + NL;
			}
			return text;
		}
		catch (IOException ioe) {
			LOG.error("Misconfiguration: unable to read help page at " + helpPage, ioe);
			return MessageFormat.format(NOT_FOUND_HELP_TEXT, page, helpPage);
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * Sets the page caller requires help with
	 * 
	 * @param referringPage page about which help is desired
	 */
	public void setPage(String referringPage) {
		_referringPage = referringPage;
	}
	
	/**
	 * Returns textual help
	 * 
	 * @return help text
	 */
	public String getHelpPageText() {
		return _helpPageText;
	}

}
