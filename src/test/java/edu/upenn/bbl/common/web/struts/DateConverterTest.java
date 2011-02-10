package edu.upenn.bbl.common.web.struts;

import org.junit.Test;
import org.junit.Assert;

public class DateConverterTest {
	
	@Test
	public void testFormatting() throws Exception {
		doTest("1978-03-28", new DateTypeConverter());
	}
	
	@Test
	public void testCustomFormatting() throws Exception {
		doTest("03/28/1978", new DateTypeConverter() {
			@Override protected String getDateFormatString() {
				return "MM/dd/yyyy";}});
	}
	
	private void doTest(String dateStr, DateTypeConverter converter) {
		System.out.println("Starting with " + dateStr);

		java.util.Date utilDate = (java.util.Date)converter.convertFromString(null, new String[]{dateStr}, java.util.Date.class);
		System.out.println("java.util.Date.toString(): " + utilDate);
		
		java.sql.Date sqlDate = (java.sql.Date)converter.convertFromString(null, new String[]{dateStr}, java.sql.Date.class);
		System.out.println("java.sql.Date.toString(): " + sqlDate);

		System.out.println("Reformat back?");
		
		String utilStr = converter.convertToString(null, utilDate);
		System.out.println("java.util.Date: " + utilStr);

		String sqlStr = converter.convertToString(null, sqlDate);
		System.out.println("java.sql.Date: " + sqlStr);
		
		Assert.assertEquals(dateStr, utilStr);
		Assert.assertEquals(dateStr, sqlStr);
	}
	
}
