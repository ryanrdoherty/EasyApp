package edu.upenn.bbl.common.util;

import org.apache.commons.lang.StringUtils;


/**
 * Wrapper for static utility methods related to Java enums.
 * 
 * @author rdoherty
 */
public class EnumUtil {

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
}
