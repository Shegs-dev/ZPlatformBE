package com.companyz.zplatform.controllers;

import com.companyz.zplatform.dtos.AuthenticateLoginDTO;
import com.companyz.zplatform.dtos.LoginDTO;
import com.companyz.zplatform.dtos.ResetPasswordDTO;
import com.companyz.zplatform.exceptions.*;
import com.companyz.zplatform.services.LoginService;
import com.mailjet.client.errors.MailjetException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Ajayi Segun on 10th December 2022
 */

@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Login Endpoints", description = "These endpoints manages login on ZPlatform")
@RequestMapping(path = "/login")
@RestController("loginRestController")
public class LoginRestController {

    //private fields
    @Autowired
    private LoginService loginService;

    @RequestMapping(method = RequestMethod.POST)
    @Operation(description = "This Service logs a user in on ZPlatform")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws InvalidInputException,
            NoSuchAlgorithmException, InvalidKeySpecException, RecordNotFoundException, CredentialsIncorrectException, MailjetException {
        log.info("API Call To Login User");

        return ResponseEntity.ok(loginService.login(loginDTO));
    }

    @RequestMapping(value = "/authenticate-login", method = RequestMethod.POST)
    @Operation(description = "This Service authenticates a user login on ZPlatform")
    public ResponseEntity<?> authenticateLogin(@RequestBody AuthenticateLoginDTO authenticateLoginDTO) throws InvalidInputException,
            RecordNotFoundException, CredentialsIncorrectException, AccessExpiredException {
        log.info("API Call To Authenticate User Login");

        return ResponseEntity.ok(loginService.authenticateLogin(authenticateLoginDTO));
    }

    @RequestMapping(value = "/initiate-reset-password", method = RequestMethod.POST)
    @Operation(description = "This Service initiates a user password reset on ZPlatform")
    public ResponseEntity<?> initiateResetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws InvalidInputException,
            RecordNotFoundException, DuplicateRequestException, MailjetException {
        log.info("API Call To Initiate User Password Reset");

        return ResponseEntity.ok(loginService.initiateResetPassword(resetPasswordDTO));
    }

    @RequestMapping(value = "/complete-reset-password", method = RequestMethod.POST)
    @Operation(description = "This Service completes a user password reset on ZPlatform")
    public ResponseEntity<?> completeResetPassword(@RequestBody LoginDTO loginDTO) throws InvalidInputException,
            RecordNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("API Call To Complete User Password Reset");

        return ResponseEntity.ok(loginService.completeResetPassword(loginDTO));
    }
}
