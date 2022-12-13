/**
 * 
 */
package com.companyz.zplatform.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author OluwasegunAjayi on 16th of March 2021
 *
 */
@Component
public class EmailValidator {
	
	private static final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	
	public boolean validate(String email) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}

}
