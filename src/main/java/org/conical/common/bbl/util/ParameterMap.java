package org.conical.common.bbl.util;

import java.util.HashMap;

/**
 * Utility class and builder pattern to easily build maps of string->object.
 * 
 * Typical usage:
 * <code>
 *   ParameterMap myMap = ParameterMap.builder()
 *     .add(key1, obj1)
 *     .add(key2, obj2)
 *     .add(key3, obj3)
 *     .getMap();
 * </code>
 * 
 * @author rdoherty
 *
 */
public class ParameterMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 20100511L;

	/**
	 * Convenience class for building ParameterMaps
	 * 
	 * @author rdoherty
	 */
	public static class ParameterMapBuilder {

		private ParameterMap _parent;
		
		/**
		 * Constructs a ParameterMapBuilder that creates a new
		 * ParameterMap to append values to.
		 */
		public ParameterMapBuilder() {
			_parent = new ParameterMap();
		}

		/**
		 * Constructs a ParameterMapBuilder that will append values
		 * to an already existing ParameterMap.
		 * 
		 * @param parent existing ParameterMap
		 */
		public ParameterMapBuilder(ParameterMap parent) {
			_parent = parent;
		}
		
		/**
		 * Adds a new value to the enclosed ParameterMap.  Like Map.put(),
		 * if the parameter key already exists, its value will be overwritten.
		 * 
		 * @param paramName key for this parameter
		 * @param paramValue value for this parameter
		 * @return a reference to this ParameterMapBuilder
		 */
		public ParameterMapBuilder add(String paramName, Object paramValue) {
			_parent.put(paramName, paramValue);
			return this;
		}
		
		/**
		 * @return the enclosed ParameterMap
		 */
		public ParameterMap getMap() {
			return _parent;
		}
	}
	
	/**
	 * Returns a ParameterMapBuilder wrapping a new ParameterMap
	 * 
	 * @return a ParameterMapBuilder wrapping a new ParameterMap
	 */
	public static ParameterMapBuilder builder() {
		return new ParameterMapBuilder();
	}

	/**
	 * @return an empty ParameterMap for general use
	 */
	public static ParameterMap getEmptyMap() {
		return new ParameterMap();
	}
	
}
