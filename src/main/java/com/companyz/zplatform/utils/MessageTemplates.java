/**
 * 
 */
package com.companyz.zplatform.utils;

import com.companyz.zplatform.dtos.MessageDTO;
import org.springframework.stereotype.Component;

/**
 * @author OluwasegunAjayi on 5th October 2021
 *
 */
@Component
public class MessageTemplates {
	
	public String getTemplate(int type, MessageDTO messageDTO) {
		String msg = "";

		if(type == 1) {//User Registration
			msg += "<p>Hello " + messageDTO.getFname() + ",</p>";
			msg += "<p>You have successfully registered and your account is verified!</p>";
			msg += "<p>Go to ZPlatform Login or Click <a href='" + messageDTO.getUrl() + "'>here</a> to login.</p>";
		}else if(type == 2) {//Password Reset
			msg += "<p>Hello " + messageDTO.getFname() + ",</p>";
			msg += "<p>You password has been reset. Please Click <a href='" + messageDTO.getUrl() + "?id=" + messageDTO.getUserId() + "'>here</a> to input new password.</p>";
		}else if(type == 3) {//Login OTP
			msg += "<p>Hello " + messageDTO.getFname() + ",</p>";
			msg += "<p>Your OTP to complete login is " + messageDTO.getDummyPassword() + " on ZPlatform.</p>";
		}

		msg += "<p><br>Regards,</p>";
		msg += "<p>CompanyZ Team</p>";

		return msg;
	}

}
