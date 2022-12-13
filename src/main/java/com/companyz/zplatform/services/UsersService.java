package com.companyz.zplatform.services;

import com.companyz.zplatform.dtos.ResponseDTO;
import com.companyz.zplatform.dtos.SignUpDTO;
import com.companyz.zplatform.dtos.VerificationDTO;
import com.companyz.zplatform.entities.Users;
import com.companyz.zplatform.enums.VerificationStatus;
import com.companyz.zplatform.exceptions.InvalidInputException;
import com.companyz.zplatform.exceptions.RecordExistException;
import com.companyz.zplatform.exceptions.RecordNotFoundException;
import com.companyz.zplatform.exceptions.UnverifiedException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

public interface UsersService {

    //Service Methods
    Users register(SignUpDTO newUser) throws InvalidInputException, RecordExistException, NoSuchAlgorithmException, InvalidKeySpecException;
    Users uploadVerificationDocuments(VerificationDTO verificationDTO) throws InvalidInputException, RecordNotFoundException;
    Users verify(String userId, VerificationStatus status) throws InvalidInputException, RecordNotFoundException;
    ResponseDTO checkVerification(String userId) throws RecordNotFoundException, UnverifiedException;
    Users get(String userId) throws RecordNotFoundException;
}
