package com.companyz.zplatform.services;

import com.companyz.zplatform.dtos.AuthenticateLoginDTO;
import com.companyz.zplatform.dtos.LoginDTO;
import com.companyz.zplatform.dtos.ResetPasswordDTO;
import com.companyz.zplatform.dtos.ResponseDTO;
import com.companyz.zplatform.entities.Login;
import com.companyz.zplatform.entities.Users;
import com.companyz.zplatform.exceptions.*;
import com.mailjet.client.errors.MailjetException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

public interface LoginService {

    //Service Methods
    ResponseDTO login(LoginDTO loginDTO) throws InvalidInputException, RecordNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, CredentialsIncorrectException, MailjetException;
    Login create(Login login) throws InvalidInputException, RecordExistException, NoSuchAlgorithmException, InvalidKeySpecException;
    Users authenticateLogin(AuthenticateLoginDTO authenticateLoginDTO) throws InvalidInputException, RecordNotFoundException, CredentialsIncorrectException, AccessExpiredException;
    ResponseDTO initiateResetPassword(ResetPasswordDTO resetPasswordDTO) throws InvalidInputException, RecordNotFoundException, DuplicateRequestException, MailjetException;
    ResponseDTO completeResetPassword(LoginDTO loginDTO) throws InvalidInputException, RecordNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException;
}
