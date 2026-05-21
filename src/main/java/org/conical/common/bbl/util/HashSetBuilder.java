package org.conical.common.bbl.util;

import java.util.Collection;
import java.util.HashSet;

public class HashSetBuilder<T> {
	
	private HashSet<T> _set;
	
	public HashSetBuilder() {
		_set = new HashSet<T>();
	}

	public HashSetBuilder<T> add(T item) {
		_set.add(item);
		return this;
	}

	public HashSetBuilder<T> addAll(Collection<? extends T> items) {
		_set.addAll(items);
		return this;
	}
	
	public HashSet<T> toHashSet() {
		return _set;
	}
	
}
