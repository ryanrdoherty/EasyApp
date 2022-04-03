package org.conical.common.bbl.db.querier;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Represents a column in a database table, and the attributes one may
 * want to assign to it.
 * 
 * @author rdoherty
 */
public class ColumnModel implements Comparable<ColumnModel> {

	public String name;
	public boolean isKey = false;
	public boolean nullAllowed = true;
	public DataType type;
	public String range;
	public int size1;
	public int size2;
	public long colnum;
	
	@Override
	public int compareTo(ColumnModel o) {
		return name.compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
