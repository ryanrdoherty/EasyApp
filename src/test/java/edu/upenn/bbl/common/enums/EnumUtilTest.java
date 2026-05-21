package edu.upenn.bbl.common.enums;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.conical.common.bbl.util.EnumUtil;
import org.junit.jupiter.api.Test;

public class EnumUtilTest {
	
	private enum MyEnum {
		A, B, C;
	}
	
	@Test
	public void testEnumConversion() throws Exception {
		MyEnum e1 = EnumUtil.getDefaultIfNullOrBad(MyEnum.A, null);
		MyEnum e2 = EnumUtil.getDefaultIfNullOrBad(MyEnum.A, "A");
		MyEnum e3 = EnumUtil.getDefaultIfNullOrBad(MyEnum.A, "B");
		MyEnum e4 = EnumUtil.getDefaultIfNullOrBad(MyEnum.A, "D");
		assertTrue(e1.equals(MyEnum.A));
		assertTrue(e2.equals(MyEnum.A));
		assertTrue(e3.equals(MyEnum.B));
		assertTrue(e4.equals(MyEnum.A));
	}
}
