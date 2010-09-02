package edu.upenn.bbl.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.upenn.bbl.common.util.WtarScoreConverter;

/**
 * Tests methods in the WtarScoreConverter class.
 * 
 * @author rdoherty
 */
public class WtarScoreConverterTest {

	@Test
	public void testGetStandardizedScore() {
		assertEquals(WtarScoreConverter.getStandardScore(12, 37), 68);
		assertEquals(WtarScoreConverter.getStandardScore(28, 72), 93);
		assertEquals(WtarScoreConverter.getStandardScore(0, 18), 50);
		assertEquals(WtarScoreConverter.getStandardScore(39, 26), 109);
		assertEquals(WtarScoreConverter.getStandardScore(45, 55), 119);
	}
	
	@Test
	public void testGetReferenceGroup() {
		assertEquals(WtarScoreConverter.getReferenceGroup(8), 58);
		assertEquals(WtarScoreConverter.getReferenceGroup(20), 78);
		assertEquals(WtarScoreConverter.getReferenceGroup(26), 88);
		assertEquals(WtarScoreConverter.getReferenceGroup(38), 108);
		assertEquals(WtarScoreConverter.getReferenceGroup(50), 128);
	}
	
	@Test
	public void testStandardizationRanges() {
		assertEquals(stdSucceeds(25, 25), true);
		assertEquals(stdSucceeds(-15, 38), false);
		assertEquals(stdSucceeds(53, 26), false);
		assertEquals(stdSucceeds(23, 14), false);
		assertEquals(stdSucceeds(9, 95), false);
	}

	@Test
	public void testRefGroupRanges() {
		assertEquals(refGroupSucceeds(11), true);
		assertEquals(refGroupSucceeds(-18), false);
		assertEquals(refGroupSucceeds(55), false);
	}

	private boolean stdSucceeds(int rawScore, int age) {
		try {
			WtarScoreConverter.getStandardScore(rawScore, age);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	private boolean refGroupSucceeds(int rawScore) {
		try {
			WtarScoreConverter.getReferenceGroup(rawScore);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	@Test
	public void testIqConversion() {
		assertEquals(WtarScoreConverter.getFsIq(12, 37), 77);
		assertEquals(WtarScoreConverter.getFsIq(28, 72), 95);
		
		assertEquals(WtarScoreConverter.getFsIq(112), 109);
		assertEquals(WtarScoreConverter.getFsIq(88), 91);
	}
	
	public void testIqRange() {
		try {
			WtarScoreConverter.getFsIq(45);
			assertTrue(false);
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}	
}
