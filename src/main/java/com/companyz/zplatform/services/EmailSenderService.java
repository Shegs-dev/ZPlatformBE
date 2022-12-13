package com.companyz.zplatform.services;

import com.companyz.zplatform.dtos.MailDTO;
import com.mailjet.client.errors.MailjetException;

/** Ajayi Segun on 3rd March 2022 **/

public interface EmailSenderService {

    void sendSimpleEmail(MailDTO mailDTO) throws MailjetException;
}
