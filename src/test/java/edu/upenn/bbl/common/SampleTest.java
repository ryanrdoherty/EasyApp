package edu.upenn.bbl.common;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Example unit test that can be copied and modified to test new classes
 * 
 * @author rdoherty
 */
public class SampleTest {

	@Test
	public void sampleTest() throws Exception {
		String band = StringUtils.join(new String[]{"a", "a"}, "bb");
		assertEquals(band, "abba");
	}
}
