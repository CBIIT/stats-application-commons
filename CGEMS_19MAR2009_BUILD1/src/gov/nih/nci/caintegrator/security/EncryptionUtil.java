package gov.nih.nci.caintegrator.security;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.*;

/**
 * 
 * @author guruswamis
 * 
 */
public class EncryptionUtil {
	// 8-byte Salt
	final static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8,
			(byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	final static int iterationCount = 19;

	private static DesEncrypter encrypter;

	private static class DesEncrypter {
		Cipher ecipher;

		Cipher dcipher;

		DesEncrypter(String passPhrase) {
			try {
				// Create the key
				KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(),
						salt, iterationCount);
				SecretKey key = SecretKeyFactory
						.getInstance("PBEWithMD5AndDES")
						.generateSecret(keySpec);

				ecipher = Cipher.getInstance(key.getAlgorithm());
				dcipher = Cipher.getInstance(key.getAlgorithm());

				// Prepare the parameter to the ciphers
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

				// Create the ciphers
				ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
				dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			} catch (java.security.InvalidAlgorithmParameterException e) {
			} catch (java.security.spec.InvalidKeySpecException e) {
			} catch (javax.crypto.NoSuchPaddingException e) {
			} catch (java.security.NoSuchAlgorithmException e) {
			} catch (java.security.InvalidKeyException e) {
			}
		}
		public String encrypt(String str) {
			try {
				// Encode the string into bytes using utf-8
				byte[] utf8 = str.getBytes("UTF8");
				// Encrypt
				byte[] enc = ecipher.doFinal(utf8);
				// Encode bytes to base64 to get a string
				return new sun.misc.BASE64Encoder().encode(enc);
			} catch (javax.crypto.BadPaddingException e) {
			} catch (IllegalBlockSizeException e) {
			} catch (UnsupportedEncodingException e) {
			}
			return null;
		}

		public String decrypt(String str) {
			try {
				// Decode base64 to get bytes
				byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
				// Decrypt
				
				byte[] utf8 = dcipher.doFinal(dec);
				// Decode using utf-8
				return new String(utf8, "UTF8");
			} catch (javax.crypto.BadPaddingException e) {
			} catch (IllegalBlockSizeException e) {
			} catch (UnsupportedEncodingException e) {
			} catch (java.io.IOException e) {
			}
			return null;
		}
	}

	//static {
		// Create encrypter/decrypter class
	//	String key = System.getProperty("gov.nih.nci.ispyportal.gp.desencrypter.key");
	//	encrypter = new DesEncrypter(key);
	//}

	/**
	 * 
	 * @param the
	 *            message to encrypt
	 * 
	 * @return the encrypted/encoded UFT8/base64 string
	 * 
	 */

	public static String encrypt(String message, String key) {
		if (encrypter == null){
			encrypter = new DesEncrypter(key);
		}
		return encrypter.encrypt(message);
	}

	/**
	 * 
	 * @param the
	 *            encrypted message
	 * 
	 * @return decrypted String
	 * 
	 */
	public static String decrypt(String message) {
		return encrypter.decrypt(message);
	}

	public static void main(String[] args) {
		String[] toEncrypt = { "RBTuser", "harrismic", "guruswamis", "liuh",
				"sahnih" };
		for (String s : toEncrypt) {
			final String e = encrypt(s + ":GP30:RBT", "My Really Long Key");
			System.out.println("The secret message " + s + " encrypted:" + e);
			try {
				System.out.println("URL encoded message:"
						+ URLEncoder.encode(e, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			System.out.println("The secret message decoded:" + decrypt(e));
		}
	}
}
