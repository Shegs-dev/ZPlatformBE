package com.companyz.zplatform.entities;

import com.companyz.zplatform.enums.MaritalStatus;
import com.companyz.zplatform.enums.VerificationStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Data
@Document
public class Users {

    //private fields
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;//This value must be unique
    private Instant dateOfBirth;
    private Media profilePhoto;
    private MaritalStatus maritalStatus;
    private String nationality;
    private VerificationStatus verificationStatus;
    private Identification identificationDetails;
    private Instant createdTime;
    private int deleteFlag;

    @Data
    public static class Identification {
        private String type;//NIN (National Identification Number) or PN (Passport Number)
        private String number;
        private Media document;
    }

}
