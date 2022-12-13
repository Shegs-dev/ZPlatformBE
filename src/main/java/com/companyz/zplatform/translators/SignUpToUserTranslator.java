package com.companyz.zplatform.translators;

import com.companyz.zplatform.dtos.SignUpDTO;
import com.companyz.zplatform.entities.Users;
import com.companyz.zplatform.enums.VerificationStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Ajayi Segun on 10th December 2022
 */

@Component
public class SignUpToUserTranslator {

    public Users convertSignUpDTOToUser(SignUpDTO signUpDTO){
        Users user = new Users();

        user.setCreatedTime(Instant.now());
        user.setDateOfBirth(Instant.ofEpochMilli(signUpDTO.getDateOfBirth()));
        user.setDeleteFlag(0);
        user.setEmail(signUpDTO.getEmail());
        user.setGender(signUpDTO.getGender());
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        user.setMaritalStatus(signUpDTO.getMaritalStatus());
        user.setNationality(signUpDTO.getNationality());
        user.setProfilePhoto(signUpDTO.getProfilePhoto());
        user.setVerificationStatus(VerificationStatus.UNVERIFIED);

        return user;
    }
}
