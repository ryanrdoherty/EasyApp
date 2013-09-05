package edu.upenn.bbl.common.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import edu.upenn.bbl.common.exception.BBLRuntimeException;

/**
 * Encryption utility class provides methods related to string and plain-text encryption.
 * This class uses the <code>java.security.MessageDigest</code> class and thus supports only
 * a subset of the algorithms supported by that class.
 * 
 * @author rdoherty
 */
public class EncryptionUtil {

	/**
	 * Represents an encryption algorithm
	 * 
	 * @author rdoherty
	 */
	public enum Algorithm {
		SHA ("SHA"),
		MD5 ("MD5");
		
		private String _algInputName;

		private Algorithm(String algInputName) {
			_algInputName = algInputName;
		}
		
		/**
		 * @return the string that must be entered into MessageDigest.getInstance()
		 * in order to create a valid digest.
		 */
		public String getAlgInputName() {
			return _algInputName;
		}
	}
	
	/**
	 * Encrypts the given plain text using the passed algorithm.  The result is then
	 * encoded as a Base-64 string and returned.
	 * 
	 * @param plaintext text to encrypt
	 * @param algorithm algorithm to use
	 * @return Base-64 encoded result of the encryption algorithm
	 */
	public static String encrypt(String plaintext, Algorithm algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm.getAlgInputName());
			md.update(plaintext.getBytes("UTF-8"));
			// Used to use this (in codec v1.4): Base64.encodeBase64String(md.digest());
			String encodingWithCRLF = new String(Base64.encodeBase64(md.digest(), true));
			// remove CRLF before returning encrypted value
			return encodingWithCRLF.substring(0, encodingWithCRLF.length() - 2);
		}
		catch (NoSuchAlgorithmException e) {
			// this should never happen
			throw new BBLRuntimeException("Java no longer supports UTF-8.", e);
		}
		catch (UnsupportedEncodingException e) {
			// this should never happen
			throw new BBLRuntimeException("Java no longer supports " + algorithm.getAlgInputName(), e);
		}
	}
}
