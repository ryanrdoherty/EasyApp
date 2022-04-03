package org.conical.common.bbl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for static utility methods related to string parsing
 * 
 * @author rdoherty
 */
public class ParseUtil {

	private enum Direction {
		NORTH,
		SOUTH,
		EAST,
		WEST;
	}
	
	private static List<String> STREET_PREFIX_LIST = getStreetPrefixList();
	
	private static List<String> getStreetPrefixList() {
		List<String> prefixList = new ArrayList<String>();
		for (Direction d : Direction.values()) {
			prefixList.add(d.name());
			prefixList.add(String.valueOf(d.name().charAt(0)));
			prefixList.add(d.name().charAt(0) + ".");
		}
		return prefixList;
	}
	
	/**
	 * Attempts to find the street name in the given String.  The algorithm is
	 * as follows:
	 * <ol>
	 *   <li>Splits address line into tokens delimited by &lt;space&gt;</li>
	 *   <li>If only 0 or 1 tokens exist, return the original value</li>
	 *   <li>If 2 tokens exist, assume first token is street number and return second token</li>
	 *   <li>If >2 tokens exist, assume first token is street number and check second for a variation of a compass direction</li>
	 *   <li>If second token is a variation of north, south, east, or west, return third token</li>
	 *   <li>If not, return second token</li>
	 * </ol>
	 * 
	 * While simplistic, this algorithm has a reasonably high degree of success.
	 * 
	 * @param address address line to be parsed
	 * @return street name (without street type (e.g. Street, Blvd, Road, Lane)
	 */
	public static String parseStreetName(String address) {
		String[] tokens = address.split(" ");
		switch (tokens.length) {
			case 0: // fall through
			case 1:
				return address;
			case 2:
				return tokens[1];
			default:
				return (STREET_PREFIX_LIST.contains(tokens[1]) ? tokens[2] : tokens[1]);
		}
	}
}
