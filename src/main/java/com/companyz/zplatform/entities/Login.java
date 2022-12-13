package com.companyz.zplatform.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Data
@Document
public class Login {

    //private fields
    @Id
    private String id;
    private String username;//email of user
    private String password;
    private List<String> oldPasswords;//password history
    private String token;
    private Instant tokenExpires;
    private boolean isResetActive;
    private Instant createdTime;
    private int deleteFlag;
}
