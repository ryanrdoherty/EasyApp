package edu.upenn.bbl.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Provides static methods related to reading and validating values read from a resource bundle
 * 
 * @author rdoherty
 */
public class PropertyMapLoader {

	/**
	 * Finds and reads the file with the given bundle name and loads properties
	 * into a key-value map, which it returns
	 * 
	 * @param resourceBundleName bundle name
	 * @return map of properties
	 */
	public static Map<String, String> loadProperties(String resourceBundleName) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName);
		Enumeration<String> e = bundle.getKeys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			propertyMap.put(key, (String)bundle.getObject(key));
		}
		return propertyMap;
	}

	/**
	 * Checks map for a list of required properties (sent in by key name).
	 * 
	 * @param map map of properties to check
	 * @param propKeys array of keys to look for
	 * @return true if all properties listed exist and are non-null, else false
	 */
	public static boolean requiredPropertiesPresent(Map<String, String> map, String... propKeys) {
		for (String propKey : propKeys) {
			if (!map.containsKey(propKey) || map.get(propKey) == null) {
				return false;
			}
		}
		return true;
	}
	
}
