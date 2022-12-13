package com.companyz.zplatform.services.implementations;

/** Ajayi Segun on 3rd March 2022 **/

import com.companyz.zplatform.dtos.MailDTO;
import com.companyz.zplatform.environment.APIs;
import com.companyz.zplatform.services.EmailSenderService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    //private fields
    @Autowired
    private APIs apis;

    @Override
    public void sendSimpleEmail(MailDTO mailDTO) throws MailjetException {
        log.info("Sending Email");

        MailjetRequest request;
        MailjetResponse response;

        ClientOptions options = ClientOptions.builder()
                .apiKey(apis.getMailjetApiKey())
                .apiSecretKey(apis.getMailjetApiSecret())
                .build();

        MailjetClient client = new MailjetClient(options);

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", apis.getSenderEmail())
                                        .put("Name", apis.getSenderName()))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", mailDTO.getTo())
                                                .put("Name", mailDTO.getReceiverName())))
                                .put(Emailv31.Message.SUBJECT, mailDTO.getSubject())
                                //.put(Emailv31.Message.TEXTPART, "Greetings from Mailjet!")
                                .put(Emailv31.Message.HTMLPART, mailDTO.getMsg())));
        log.info("Request {} " , request.toString());
        response = client.post(request);
        log.info("Sent Email Successfully Status : {} and Data {}" , response.getStatus(), response.getData());
    }
}
