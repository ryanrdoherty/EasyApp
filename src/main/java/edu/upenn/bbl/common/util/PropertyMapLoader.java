package edu.upenn.bbl.common.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides static methods related to reading and validating values read from a resource bundle
 * 
 * @author rdoherty
 */
public class PropertyMapLoader {

	private static final Logger LOG = LoggerFactory.getLogger(PropertyMapLoader.class);
	
	/**
	 * Finds and reads the file with the given bundle name and loads properties
	 * into a key-value map, which it returns
	 * 
	 * @param resourceBundleName bundle name
	 * @return map of properties
	 */
	public static Map<String, String> loadProperties(String resourceBundleName) {
		return convertBundleToMap(ResourceBundle.getBundle(resourceBundleName));
	}

	private static Map<String, String> convertBundleToMap(ResourceBundle bundle) {
		Map<String, String> propertyMap = new HashMap<String, String>();
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
	
	/**
	 * Loads properties from a resource which are defined by the values of an
	 * enum.  If not all properties in the enum can be found, or the resource
	 * cannot be read, a runtime exception is thrown.
	 * 
	 * @param resourceName name of the resource
	 * @param enumClass class of enum defining the property names
	 * @return map from enum values to property values
	 */
	public static <T extends Enum<?>> Map<T, String> loadEnumProperties(String resourceName, Class<T> enumClass) {
		try {
			Properties props = new Properties();
			props.load(getClassLoader().getResourceAsStream(resourceName));
			LOG.debug("Loaded properties in " + resourceName + ", and found " + props.size() + " items.");
			Map<T, String> propertyMap = new HashMap<>();
			T[] requiredKeys = enumClass.getEnumConstants();
			for (T key : requiredKeys) {
				LOG.debug("Looking for " + key + ": found " + props.get(key.name()));
				if (!props.containsKey(key.name())) {
					throw new RuntimeException("Missing key in properties file (" + resourceName + "): " + key);
				}
				propertyMap.put(key, props.getProperty(key.name()));
			}
			return propertyMap;
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to load properties.", e);
		}
	}

	private static ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) loader = ClassLoader.getSystemClassLoader();
		return loader;
	}
}
