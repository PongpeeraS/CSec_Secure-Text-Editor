import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/* Class to encrypt and decrypt Strings 
 * using the AES algorithm with ECB & PCKS5 padding scheme. 
 */
public class AES {
	private static SecretKeySpec secretKey;
	private static byte[] key;

	/* Create & set this object's secret key using the given String */
	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			/*
			 * Convert the given String into a Byte array, create MD instance (w/ SHA-1
			 * algorithm) & compute MD using given key String. Finally, trim/pad the key to
			 * the size of 16 bytes and construct the secret key using AES.
			 */
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/* Encrypt the given String using the given key */
	public static String encrypt(String strToEncrypt, String secret) {
		try {
			setKey(secret);// Set the secret key for this operation
			/*
			 * Create a Cipher object using AES w/ ECB + PKCS5 padding scheme Then
			 * initialize the object (as encrypt mode) using the created secret key
			 */
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			/*
			 * Encrypt the (Byte) String into a Base64 Byte array, then encrypt again using
			 * the created AES cipher
			 */
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	/* Decrypt the given String using the given key */
	public static String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret); // Set the secret key for this operation
			/*
			 * Create a Cipher object using AES w/ ECB + PKCS5 padding scheme Then
			 * initialize the object (as decrypt mode) using the created secret key
			 */
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			/*
			 * Decrypt the Base64 String into a Byte array, then decrypt again using the
			 * created AES cipher
			 */
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String[] args) {
		String key = "ICT";
		String str = "Mahidol\n\tUniversity";
		String enc = AES.encrypt(str, key);
		String dec = AES.decrypt(enc, key);
		System.out.println(str + "\n" + enc + "\n" + dec);
	}
}