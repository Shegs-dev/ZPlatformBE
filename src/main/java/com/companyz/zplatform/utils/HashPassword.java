/**
 * 
 */
package com.companyz.zplatform.utils;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * @author OluwasegunAjayi on 21st May 2020
 *
 */
@Component
public class HashPassword {
	
	public String hashPass(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
		System.out.println("Hashing password");
		
		return generateStrongPasswordHash(password);
    }
	
	public Boolean validatePass(String password, String dbpassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("Validating The Password");
			
		return validatePassword(password, dbpassword);
	}
		

	private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    int iterations = 1000;
	    char[] chars = password.toCharArray();
	    byte[] salt = getSalt();
	        
	    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
	    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] hash = skf.generateSecret(spec).getEncoded();
	    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}
	    
	private static byte[] getSalt() throws NoSuchAlgorithmException {
	    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	    byte[] salt = new byte[16];
	    sr.nextBytes(salt);
	    return salt;
	}
	    
	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
	    BigInteger bi = new BigInteger(1, array);
	    String hex = bi.toString(16);
	    int paddingLength = (array.length * 2) - hex.length();
	    if(paddingLength > 0) {
	        return String.format("%0"  +paddingLength + "d", 0) + hex;
	    }else{
	        return hex;
	    }
	}
	   
	//Validation of Password
	private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    String[] parts = storedPassword.split(":");
	    int iterations = Integer.parseInt(parts[0]);
	    byte[] salt = fromHex(parts[1]);
	    byte[] hash = fromHex(parts[2]);
		         
	    PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
	    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] testHash = skf.generateSecret(spec).getEncoded();
		         
	    int diff = hash.length ^ testHash.length;
	    for(int i = 0; i < hash.length && i < testHash.length; i++) {
		    diff |= hash[i] ^ testHash[i];
	    }
	    return diff == 0;
	}
	
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
	    byte[] bytes = new byte[hex.length() / 2];
	    for(int i = 0; i<bytes.length ;i++) {
		    bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	    }
	    return bytes;
	}

}
