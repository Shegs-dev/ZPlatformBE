/**
 * 
 */
package com.companyz.zplatform.dtos;

import lombok.Data;

/**
 * @author OluwasegunAjayi on 5th October 2021
 *
 */

@Data
public class MailDTO {
	
	//private fields
	private String to;
	private String msg;
	private String subject;
	private String cc;
	private String bcc;
	private String from;
	private String receiverName;
}
