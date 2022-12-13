package com.companyz.zplatform.dtos;

import com.companyz.zplatform.entities.Media;
import com.companyz.zplatform.enums.MaritalStatus;
import lombok.Data;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Data
public class SignUpDTO {

    //private fields
    private String firstName;
    private String lastName;
    private String gender;
    private String email;//This value must be unique
    private long dateOfBirth;
    private Media profilePhoto;
    private MaritalStatus maritalStatus;
    private String nationality;
    private String password;
}
