package com.companyz.zplatform.controllers;

import com.companyz.zplatform.dtos.SignUpDTO;
import com.companyz.zplatform.dtos.VerificationDTO;
import com.companyz.zplatform.enums.VerificationStatus;
import com.companyz.zplatform.exceptions.InvalidInputException;
import com.companyz.zplatform.exceptions.RecordExistException;
import com.companyz.zplatform.exceptions.RecordNotFoundException;
import com.companyz.zplatform.exceptions.UnverifiedException;
import com.companyz.zplatform.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Ajayi Segun on 10th December 2022
 */

@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Users Endpoints", description = "These endpoints manages users on ZPlatform")
@RequestMapping(path = "/users")
@RestController("usersRestController")
public class UsersRestController {

    //private fields
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Operation(description = "This Service registers a new user on ZPlatform")
    public ResponseEntity<?> register(@RequestBody SignUpDTO newUser) throws InvalidInputException, RecordExistException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("API Call To Register New User {} " , newUser);

        return ResponseEntity.ok(usersService.register(newUser));
    }

    @RequestMapping(value = "/upload-verification-document", method = RequestMethod.POST)
    @Operation(description = "This Service uploads verification document on ZPlatform")
    public ResponseEntity<?> uploadVerificationDocuments(@RequestBody VerificationDTO verificationDTO) throws InvalidInputException, RecordNotFoundException {
        log.info("API Call To Uploads Verification Document {} " , verificationDTO);

        return ResponseEntity.ok(usersService.uploadVerificationDocuments(verificationDTO));
    }

    @RequestMapping(value = "/verify/{userId}/{status}", method = RequestMethod.GET)
    @Operation(description = "This Service verifies user")
    public ResponseEntity<?> verify(@PathVariable String userId, @PathVariable VerificationStatus status) throws InvalidInputException, RecordNotFoundException {
        log.info("API Call To Verifies User");

        return new ResponseEntity<>(usersService.verify(userId, status), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/{userId}", method = RequestMethod.GET)
    @Operation(description = "This Service gets user")
    public ResponseEntity<?> get(@PathVariable String userId) throws RecordNotFoundException {
        log.info("API Call To Get User");

        return new ResponseEntity<>(usersService.get(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/check-verification/{userId}", method = RequestMethod.GET)
    @Operation(description = "This Service checks user verification as a callback request")
    public ResponseEntity<?> checkVerification(@PathVariable String userId) throws RecordNotFoundException, UnverifiedException {
        log.info("API Call To Check User Verification Status");

        return new ResponseEntity<>(usersService.checkVerification(userId), HttpStatus.OK);
    }
}
