package com.companyz.zplatform.environment;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Ajayi Segun on 16th March 2022 **/

@Component
@Data
public class APIs {

    //private fields
    @Value("${sender.email}")
    private String senderEmail;
    @Value("${mail-jet.apikey}")
    private String mailjetApiKey;
    @Value("${mail-jet.apiSecret}")
    private String mailjetApiSecret;
    @Value("${mail-jet.client.options}")
    private String mailjetClientOptions;
    @Value("${sender.name}")
    private String senderName;
    @Value("${no-reply.email}")
    private String noReply;

    @Value("${access.key.id}")
    private String accessKeyId;
    @Value("${access.key.secret}")
    private String accessKeySecret;
    @Value("${s3.region.name}")
    private String s3RegionName;
    @Value("${s3.bucket.name}")
    private String s3BucketName;

    @Value("${zplatform.url}")
    private String zplatformUrl;
    @Value("${reset-password.url}")
    private String resetPasswordUrl;
}
