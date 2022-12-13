/**
 * 
 */
package com.companyz.zplatform.utils;

import org.springframework.stereotype.Component;

/**
 * @author OluwasegunAjayi on 10th of June 2021
 *
 */
@Component
public class TokenGenerator {
	
	//private fields
	private String repo = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
	private static final int min = 1;
	private static final int max = 36;
	
	//Method to generate coupon
	public String generate() {
		System.out.println("Generating Coupon");
		
		String coupon = "";
		for(int i = 0; i < 6; i++) {
			coupon += repo.charAt(genRandomNumber() - 1);
		}
		
		return coupon;
	}
	
	//method to generate the position to work with
	public int genRandomNumber() {
		int x = (int) ((Math.random() * ((max - min) + 1)) + min);
		
		return x;
	}

}
