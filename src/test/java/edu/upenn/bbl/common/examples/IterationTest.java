package edu.upenn.bbl.common.examples;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class IterationTest {

	public class CounterIterator implements Iterator<Integer> {

		private int _currentValue;
		private int _numTimes;
		private int _increment;
		
		public CounterIterator(int firstValue, int numTimes, int increment) {
			_currentValue = firstValue;
			_numTimes = numTimes;
			_increment = increment;
		}
		
		@Override
		public boolean hasNext() {
			return !(_numTimes == 0);
		}

		@Override
		public Integer next() {
			int retVal = _currentValue;
			_currentValue += _increment;
			_numTimes--;
			return retVal;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Operation not supported.");
		}			
	}
	
	public class Counter implements Iterable<Integer> {

		private int _firstValue;
		private int _numTimes;
		private int _increment;
		
		public Counter(int numTimes) {
			this(0, numTimes, 1);
		}
		
		public Counter(int firstValue, int numTimes) {
			this(firstValue, numTimes, 1);
		}
			
		public Counter(int firstValue, int numTimes, int increment) {
			_firstValue = firstValue;
			_numTimes = numTimes;
			_increment = increment;
		}
		
		@Override
		public Iterator<Integer> iterator() {
			return new CounterIterator(_firstValue, _numTimes, _increment);
		}		
	}
	
	@Test
	public void testCounter() throws Exception {
		System.out.println("Iteration and enhanced for loop example output:");
		print(1, new Counter(5));
		print(2, new Counter(5, 10));
		print(3, new Counter(30, 6, -6));
	}
	
	private void print(int testnum, Counter counter) {
		System.out.print("Test " + testnum + "A: ");
		for (Integer i : counter) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("Test " + testnum + "B: " + StringUtils.join(counter.iterator(), " "));
	}
}
