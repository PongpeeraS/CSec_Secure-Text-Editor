/* Class to encrypt and decrypt Strings using a Vigenere Cipher. 
 * The Vigenere cipher is a polyalphabetic method of encrypting alphabetic text 
 * by using a series of different Caesar ciphers based on the letters of a keyword.
 * This cipher flattens the high frequency distribution and increases the 
 * low frequency distribution of alphabets present in Caesar Cipher.
 */
public class VigenereCipher {
	/*
	 * Encrypts a message using a Vigenere Cipher with a given String key. Assumes
	 * the String only contains characters defined in the standard ASCII table from
	 * 32-126
	 */
	public static String encrypt(String plaintext, String key) {
		String encrypted = "";
		for (int i = 0; i < plaintext.length(); i++) {
			int c = plaintext.charAt(i);
			/*
			 * If c is not in the printable ASCII range (32-126), skip
			 */
			if (c < 32 || c > 126) {
				encrypted += (char) c;
				continue;
			}
			// Get the Caesar cipher key for this plaintext char & key char
			int caesar = key.charAt(i % key.length()) - 32;
			/*
			 * For ASCII support, as printable space starts at 32, subtract 32 from the sum
			 * of keyChar and messageChar to get zero based. Then calculate the modulo of
			 * this sum to be sure to stay in bounds. Finally add 32 to the result of the
			 * modulo operation to place it in the 32 - 126 range.
			 */
			int sum = c - 32 + caesar;
			encrypted += (char) (sum % 95 + 32);
		}
		return encrypted;
	}

	/*
	 * Decrypts a message that was encoded with a Vigenere Cipher with given String
	 * key. Assumes the String ciphertext only used standard ASCII characters of
	 * values 32-126
	 */
	public static String decrypt(String ciphertext, String key) {
		String decrypted = "";
		for (int i = 0; i < ciphertext.length(); i++) {
			int c = ciphertext.charAt(i);
			/*
			 * If c is not in the printable ASCII range (32-126), skip
			 */
			if (c < 32 || c > 126) {
				decrypted += (char) c;
				continue;
			}
			// Get the Caesar cipher key for this plaintext char & key char
			int caesar = key.charAt(i % key.length()) - 32;
			/*
			 * For ASCII support, as printable space starts at 32, subtract 32 from the sum
			 * of keyChar and messageChar to get zero based. Then calculate the modulo of
			 * this sum to be sure to stay in bounds. Finally add 32 to the result of the
			 * modulo operation to place it in the 32 - 126 range.
			 */
			int sum = c - 32 + (95 - caesar);
			decrypted += (char) (sum % 95 + 32);
		}
		return decrypted;
	}

	public static void main(String[] args) {
		String key = "ICT";
		String str = "Mahidol\n\tUniversity";
		String enc = encrypt(str, key);
		String dec = decrypt(enc, key);
		System.out.println(str + "\n" + enc + "\n" + dec);
	}
}