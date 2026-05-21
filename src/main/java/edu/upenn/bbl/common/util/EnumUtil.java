package edu.upenn.bbl.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.upenn.bbl.common.enums.ifc.Coded;
import edu.upenn.bbl.common.exception.BBLRuntimeException;


/**
 * Wrapper for static utility methods related to Java enums.
 * 
 * @author rdoherty
 */
public class EnumUtil {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(EnumUtil.class.getName());
	
	/**
	 * Returns the name (String) of an enum value passed in.  Since simply
	 * calling name() can cause NullPointerExceptions, this method provides
	 * a "null-safe" way to get the name.  If null is passed in, null is
	 * returned.
	 * 
	 * @param <T> the enum class
	 * @param enumValue an enum value
	 * @return the name of the enum value, or null if null was passed
	 */
	public static <T extends Enum<?>> String getNullSafeName(T enumValue) {
		return (enumValue == null ? null : enumValue.name());
	}
	
	/**
	 * Returns the value of the enum class passed in whose name is the same
	 * as the String passed in.  If an empty or null String is passed in,
	 * returns null.
	 * 
	 * @param <T> type to be returned
	 * @param clazz class of type to be returned
	 * @param value value to be converted
	 * @return enum value
	 * @throws IllegalArgumentException if value is non-empty, but can still not be converted
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T getNullSafeEnum(Class<T> clazz, String value) {
		return (T)(StringUtils.isEmpty(value) ? null : Enum.valueOf((Class<? extends Enum>)clazz, value));
	}

	/**
	 * Returns the value of a Coded enum based on its code.
	 * 
	 * @param <T> class of the Coded enum
	 * @param clazz Class of the Coded enum
	 * @param codedValue code
	 * @return enum value
	 * @throws NoSuchElementException if no value of the given class is coded to the given value
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Coded> T getValueByCode(Class<T> clazz, int codedValue) {
		if (!clazz.isEnum()) {
			throw new IllegalArgumentException("Class passed is not an enum.");
		}
		try {
			Method m = clazz.getDeclaredMethod("values");
			T[] enumValues = (T[])m.invoke(null);
			for (T value : enumValues) {
				if (value.getCode() == codedValue) {
					return value;
				}
			}
			throw new NoSuchElementException("No enum element of type " + clazz + " is coded to value " + codedValue);
		}
		catch (SecurityException e) {
			throw new BBLRuntimeException("Unable to invoke values() on an enum class (for security reasons).", e);
		}
		catch (IllegalAccessException e) {
			throw new BBLRuntimeException("Unable to invoke values() on an enum class (for security reasons).", e);
		}
		catch (InvocationTargetException e) {
			throw new BBLRuntimeException("Unable to invoke values() on an enum class (for security reasons).", e);
		}
		catch (NoSuchMethodException e) {
			// this should never happen since we confirm above that the type is an enum
			throw new BBLRuntimeException("Unable to invoke values() on an enum class.", e);
		}
	}

	/**
	 * Converts an enum name to a more human-readable description.  The result can
	 * be handy for drop-down selects, etc.  Typically enum values are all caps with
	 * an underscore separator (e.g. "MY_ENUM").  The result of this method is a mixed
	 * case value with a space separator (e.g. "My Enum").
	 * 
	 * @param name enum name
	 * @return human readable description
	 */
	public static String getGenericDescription(String name) {
		name = name.trim().replace('_', ' ').toLowerCase();
		StringBuilder description = new StringBuilder(name);
		boolean nextCap = true;
		char c;
		for (int i = 0; i < name.length(); i++) {
			c = name.charAt(i);
			if (c == ' ') {
				nextCap = true;
			}
			else if (nextCap) {
				c = Character.toUpperCase(c);
				nextCap = false;
			}
			description.setCharAt(i, c);
		}
		return description.toString();
	}
	
	/**
	 * Examines the passed value and determines whether it can be converted into an enum
	 * value of the same type as the defaultValue.  If it can, converts it and returns.  If not,
	 * or if the value is null, returns defaultValue.
	 * 
	 * @param <T> type of value to be returned
	 * @param defaultValue returned if unable to convert value
	 * @param value string value to be converted
	 * @return Enum represented by value of the same type as defaultValue, or defaultValue
	 * if value is null or cannot be converted
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T getDefaultIfNullOrBad(T defaultValue, String value) {
		if (value == null) {
			return defaultValue;
		}
		try {
			return (T)T.valueOf(defaultValue.getClass(), value);
		}
		catch (IllegalArgumentException iae) {
			return defaultValue;
		}
	}
}
