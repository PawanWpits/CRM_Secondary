package com.wpits.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordEncodeDecode {

	// You should use a secure method to store and manage this key
	private static final String SECRET_KEY = "myStrongPassword"; // must be 16 characters

	// Method to generate a SecretKeySpec object from the provided key
	private static SecretKeySpec createKey(String key) throws Exception {
		byte[] keyBytes = key.getBytes("UTF-8");
		return new SecretKeySpec(keyBytes, "AES");
	}

	// Encrypts the password using AES
	public static String encrypt(String plainTextPassword) throws Exception {
		SecretKeySpec secretKey = createKey(SECRET_KEY);

		// Create Cipher instance for AES
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		// Encrypt the password
		byte[] encryptedBytes = cipher.doFinal(plainTextPassword.getBytes("UTF-8"));

		// Encode encrypted bytes to Base64 string
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// Decrypts the password using AES
	public static String decrypt(String encryptedPassword) throws Exception {
		SecretKeySpec secretKey = createKey(SECRET_KEY);

		// Create Cipher instance for AES
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		// Decode Base64 string to get the encrypted bytes
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);

		// Decrypt the bytes to get the original password
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);

		// Convert decrypted bytes to string
		return new String(decryptedBytes, "UTF-8");
	}

}
