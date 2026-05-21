package edu.upenn.bbl.common.auth;

import java.util.HashMap;
import java.util.Map;

import org.conical.common.bbl.auth.EncryptionUtil;
import org.conical.common.bbl.auth.EncryptionUtil.Algorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the EncryptionUtil class for a variety of algorithms.
 * 
 * @author rdoherty
 */
public class EncryptionTest {

	
	private static final String TEST_STRING = "tiger";
	
	private static final String[] OUTPUT = new String[]
	{
		"SHA", "RuPXcqGIjq3/JsetpH/XUC15bgc=",
		"MD5", "Q7kJIECWGPGIv8aSPxa5+g=="
	};
	
	private static final Map<Algorithm,String> OUTPUT_MAP = getOutput();
	
	private static Map<Algorithm, String> getOutput() {
		Map<Algorithm,String> map = new HashMap<Algorithm,String>();
		for (int i = 0; i < OUTPUT.length; i+=2) {
			map.put(Algorithm.valueOf(OUTPUT[i]), OUTPUT[i+1]);
		}
		return map;
	}
	
	@Test
	public void testEncryption() {
		for (Algorithm alg : Algorithm.values()) {
		  String encrypted = EncryptionUtil.encrypt(TEST_STRING, alg);
		  System.out.println("Test string '" + TEST_STRING + "' " +
		  		"encrypted with " + alg.toString() + " = " + encrypted);
			assertEquals(OUTPUT_MAP.get(alg), encrypted);
		}
	}
}
