package org.conical.common.bbl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles conversion of WTAR raw scores into standard scores and then FS IQ scores.
 * 
 * @author rdoherty
 */
public class WtarScoreConverter {

	private static final int REF_GROUP_INDEX = 8;
	
	private static class AgeGroup {
		private int _start;
		private int _end;
		public AgeGroup(int start, int end) {
			_start = start;
			_end = end;
		}
		public boolean contains(int age) {
			return (age >= _start && age <= _end);
		}
		public int getStart() { return _start; }
		public int getEnd() { return _end; }
	}
	
	private static final List<AgeGroup> _ageMap;
	private static final List<Integer[]> _conversionMap;
	private static final Map<Integer, Integer> _iqMap;

	static {
		_ageMap = new LinkedList<AgeGroup>();
		_ageMap.add(new AgeGroup(16, 17));
		_ageMap.add(new AgeGroup(18, 19));
		_ageMap.add(new AgeGroup(20, 24));
		_ageMap.add(new AgeGroup(25, 34));
		_ageMap.add(new AgeGroup(35, 54));
		_ageMap.add(new AgeGroup(55, 79));
		_ageMap.add(new AgeGroup(80, 84));
		_ageMap.add(new AgeGroup(85, 89));
		
		_conversionMap = new ArrayList<Integer[]>();
		_conversionMap.add(new Integer[] { 50, 50, 50, 50, 50, 52, 53, 53, 50 }); // 0
		_conversionMap.add(new Integer[] { 50, 50, 50, 50, 51, 53, 54, 54, 50 }); // 1
		_conversionMap.add(new Integer[] { 50, 50, 50, 51, 53, 54, 55, 56, 50 }); // 2
		_conversionMap.add(new Integer[] { 51, 50, 50, 53, 54, 56, 57, 57, 50 }); // 3
		_conversionMap.add(new Integer[] { 52, 50, 52, 54, 56, 57, 58, 59, 51 }); // 4
		_conversionMap.add(new Integer[] { 54, 51, 53, 56, 57, 59, 60, 60, 53 }); // 5
		_conversionMap.add(new Integer[] { 56, 53, 55, 57, 59, 60, 61, 62, 54 }); // 6
		_conversionMap.add(new Integer[] { 58, 55, 57, 59, 60, 62, 63, 63, 56 }); // 7
		_conversionMap.add(new Integer[] { 59, 57, 58, 61, 62, 63, 64, 65, 58 }); // 8
		_conversionMap.add(new Integer[] { 61, 59, 60, 62, 63, 65, 66, 66, 59 }); // 9
		_conversionMap.add(new Integer[] { 63, 60, 62, 64, 65, 66, 67, 68, 61 }); // 10
		_conversionMap.add(new Integer[] { 65, 62, 63, 65, 66, 68, 69, 69, 63 }); // 11
		_conversionMap.add(new Integer[] { 66, 64, 65, 67, 68, 69, 70, 71, 64 }); // 12
		_conversionMap.add(new Integer[] { 68, 66, 67, 68, 69, 71, 72, 72, 66 }); // 13
		_conversionMap.add(new Integer[] { 70, 67, 68, 70, 71, 72, 73, 74, 68 }); // 14
		_conversionMap.add(new Integer[] { 72, 69, 70, 72, 72, 74, 75, 75, 69 }); // 15
		_conversionMap.add(new Integer[] { 74, 71, 72, 73, 74, 75, 76, 77, 71 }); // 16
		_conversionMap.add(new Integer[] { 75, 73, 73, 75, 75, 77, 78, 78, 73 }); // 17
		_conversionMap.add(new Integer[] { 77, 74, 75, 76, 77, 78, 79, 80, 74 }); // 18
		_conversionMap.add(new Integer[] { 79, 76, 77, 78, 78, 80, 81, 81, 76 }); // 19
		_conversionMap.add(new Integer[] { 81, 78, 78, 79, 80, 81, 82, 83, 78 }); // 20
		_conversionMap.add(new Integer[] { 82, 80, 80, 81, 81, 83, 84, 84, 79 }); // 21
		_conversionMap.add(new Integer[] { 84, 81, 82, 83, 83, 84, 85, 86, 81 }); // 22
		_conversionMap.add(new Integer[] { 86, 83, 83, 84, 84, 86, 87, 87, 83 }); // 23
		_conversionMap.add(new Integer[] { 88, 85, 85, 86, 86, 87, 88, 89, 84 }); // 24
		_conversionMap.add(new Integer[] { 89, 87, 87, 87, 87, 89, 90, 90, 86 }); // 25
		_conversionMap.add(new Integer[] { 91, 89, 88, 89, 89, 90, 91, 92, 88 }); // 26
		_conversionMap.add(new Integer[] { 93, 90, 90, 91, 90, 92, 93, 93, 89 }); // 27
		_conversionMap.add(new Integer[] { 95, 92, 92, 92, 92, 93, 94, 95, 91 }); // 28
		_conversionMap.add(new Integer[] { 96, 94, 93, 94, 93, 95, 96, 96, 93 }); // 29
		_conversionMap.add(new Integer[] { 98, 96, 95, 95, 95, 96, 97, 98, 94 }); // 30
		_conversionMap.add(new Integer[] { 100, 97, 97, 97, 96, 98, 99, 99, 96 }); // 31
		_conversionMap.add(new Integer[] { 102, 99, 98, 98, 98, 99, 100, 101, 98 }); // 32
		_conversionMap.add(new Integer[] { 104, 101, 100, 100, 99, 101, 102, 102, 99 }); // 33
		_conversionMap.add(new Integer[] { 105, 103, 102, 102, 101, 102, 103, 104, 101 }); // 34
		_conversionMap.add(new Integer[] { 107, 104, 103, 103, 102, 104, 105, 105, 103 }); // 35
		_conversionMap.add(new Integer[] { 109, 106, 105, 105, 104, 105, 106, 107, 104 }); // 36
		_conversionMap.add(new Integer[] { 111, 108, 107, 106, 105, 107, 108, 108, 106 }); // 37
		_conversionMap.add(new Integer[] { 112, 110, 108, 108, 107, 108, 109, 110, 108 }); // 38
		_conversionMap.add(new Integer[] { 114, 111, 110, 109, 108, 110, 111, 111, 109 }); // 39
		_conversionMap.add(new Integer[] { 116, 113, 112, 111, 110, 111, 112, 113, 111 }); // 40
		_conversionMap.add(new Integer[] { 118, 115, 113, 113, 111, 113, 114, 114, 113 }); // 41
		_conversionMap.add(new Integer[] { 119, 117, 115, 114, 113, 114, 115, 116, 114 }); // 42
		_conversionMap.add(new Integer[] { 121, 119, 117, 116, 114, 116, 117, 117, 116 }); // 43
		_conversionMap.add(new Integer[] { 123, 120, 118, 117, 116, 117, 118, 119, 118 }); // 44
		_conversionMap.add(new Integer[] { 125, 122, 120, 119, 117, 119, 120, 120, 119 }); // 45
		_conversionMap.add(new Integer[] { 126, 124, 122, 121, 119, 120, 121, 122, 121 }); // 46
		_conversionMap.add(new Integer[] { 128, 126, 123, 122, 120, 122, 123, 123, 123 }); // 47
		_conversionMap.add(new Integer[] { 130, 127, 125, 124, 122, 123, 124, 125, 124 }); // 48
		_conversionMap.add(new Integer[] { 132, 129, 127, 125, 123, 125, 126, 126, 126 }); // 49
		_conversionMap.add(new Integer[] { 134, 131, 128, 127, 125, 126, 127, 128, 128 }); // 50
		
		_iqMap = new HashMap<Integer, Integer>();
		_iqMap.put(50,64);
		_iqMap.put(51,65);
		_iqMap.put(52,65);
		_iqMap.put(53,66);
		_iqMap.put(54,67);
		_iqMap.put(55,68);
		_iqMap.put(56,68);
		_iqMap.put(57,69);
		_iqMap.put(58,70);
		_iqMap.put(59,70);
		_iqMap.put(60,71);
		_iqMap.put(61,72);
		_iqMap.put(62,73);
		_iqMap.put(63,73);
		_iqMap.put(64,74);
		_iqMap.put(65,75);
		_iqMap.put(66,76);
		_iqMap.put(67,76);
		_iqMap.put(68,77);
		_iqMap.put(69,78);
		_iqMap.put(70,78);
		_iqMap.put(71,79);
		_iqMap.put(72,80);
		_iqMap.put(73,81);
		_iqMap.put(74,81);
		_iqMap.put(75,82);
		_iqMap.put(76,83);
		_iqMap.put(77,83);
		_iqMap.put(78,84);
		_iqMap.put(79,85);
		_iqMap.put(80,86);
		_iqMap.put(81,86);
		_iqMap.put(82,87);
		_iqMap.put(83,88);
		_iqMap.put(84,88);
		_iqMap.put(85,89);
		_iqMap.put(86,90);
		_iqMap.put(87,91);
		_iqMap.put(88,91);
		_iqMap.put(89,92);
		_iqMap.put(90,93);
		_iqMap.put(91,94);
		_iqMap.put(92,94);
		_iqMap.put(93,95);
		_iqMap.put(94,96);
		_iqMap.put(95,96);
		_iqMap.put(96,97);
		_iqMap.put(97,98);
		_iqMap.put(98,99);
		_iqMap.put(99,99);
		_iqMap.put(100,100);
		_iqMap.put(101,101);
		_iqMap.put(102,101);
		_iqMap.put(103,102);
		_iqMap.put(104,103);
		_iqMap.put(105,104);
		_iqMap.put(106,104);
		_iqMap.put(107,105);
		_iqMap.put(108,106);
		_iqMap.put(109,106);
		_iqMap.put(110,107);
		_iqMap.put(111,108);
		_iqMap.put(112,109);
		_iqMap.put(113,109);
		_iqMap.put(114,110);
		_iqMap.put(115,111);
		_iqMap.put(116,112);
		_iqMap.put(117,112);
		_iqMap.put(118,113);
		_iqMap.put(119,114);
		_iqMap.put(120,114);
		_iqMap.put(121,115);
		_iqMap.put(122,116);
		_iqMap.put(123,117);
		_iqMap.put(124,117);
		_iqMap.put(125,118);
		_iqMap.put(126,119);
		_iqMap.put(127,119);
		_iqMap.put(128,120);
		_iqMap.put(129,121);
		_iqMap.put(130,122);
		_iqMap.put(131,122);
		_iqMap.put(132,123);
		_iqMap.put(133,124);
		_iqMap.put(134,124);
	}
	
	/**
	 * Given the subject's age, converts a raw WTAR score to a standard score
	 * 
	 * @param rawScore raw score
	 * @param age age of subject
	 * @return standard score
	 */
	public static int getStandardScore(int rawScore, int age) {
		validateRawScore(rawScore);
		int ageIndex = getAgeColumnIndex(age);
		return _conversionMap.get(rawScore)[ageIndex];
	}

	/**
	 * Returns the WTAR reference group for the passed raw score
	 * 
	 * @param rawScore raw score
	 * @return reference group
	 */
	public static int getReferenceGroup(int rawScore) {
		validateRawScore(rawScore);
		return _conversionMap.get(rawScore)[REF_GROUP_INDEX];
	}
	
	/**
	 * Given the subject's age, converts a raw WTAR score to an FS IQ.
	 * This is equivalent to <code>getFsIq(getStandardScore(rawScore, age))</code>.
	 * 
	 * @param rawScore raw score
	 * @param age age of subject
	 * @return FS IQ
	 */
	public static int getFsIq(int rawScore, int age) {
		return getFsIq(getStandardScore(rawScore, age));
	}
	
	/**
	 * Converts a standard WTAR score to an FS IQ.
	 * 
	 * @param standardScore standard score
	 * @return FS IQ
	 */
	public static int getFsIq(int standardScore) {
		Integer fsIq = _iqMap.get(standardScore);
		if (fsIq == null) {
			throw new IllegalArgumentException("Standard score passed in (" +
					standardScore + ") does not have a corresponding FSIQ.");
		}
		return fsIq;
	}

	private static void validateRawScore(int rawScore) {
		if (rawScore < 0 || rawScore >= _conversionMap.size()) {
			throw new IllegalArgumentException("Raw score passed in (" + rawScore +
					") must be between 0 and "  + (_conversionMap.size() - 1) + " (inclusive).");
		}
	}

	private static int getAgeColumnIndex(int age) {
		int index = 0;
		for (AgeGroup ag : _ageMap) {
			if (ag.contains(age)) {
				return index;
			}
			index++;
		}
		throw new IllegalArgumentException("Age passed in (" + age + ") must be between " +
				_ageMap.get(0).getStart() + " and " + _ageMap.get(_ageMap.size()-1).getEnd() + " (inclusive).");
	}
}
