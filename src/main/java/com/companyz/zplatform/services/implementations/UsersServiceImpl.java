package com.companyz.zplatform.services.implementations;

import com.companyz.zplatform.dtos.ResponseDTO;
import com.companyz.zplatform.dtos.SignUpDTO;
import com.companyz.zplatform.dtos.VerificationDTO;
import com.companyz.zplatform.entities.Login;
import com.companyz.zplatform.entities.Users;
import com.companyz.zplatform.enums.VerificationStatus;
import com.companyz.zplatform.environment.Constants;
import com.companyz.zplatform.exceptions.GeneralFailureException;
import com.companyz.zplatform.exceptions.InvalidInputException;
import com.companyz.zplatform.exceptions.RecordExistException;
import com.companyz.zplatform.exceptions.RecordNotFoundException;
import com.companyz.zplatform.exceptions.UnverifiedException;
import com.companyz.zplatform.repositories.UsersRepository;
import com.companyz.zplatform.services.UsersService;
import com.companyz.zplatform.translators.SignUpToUserTranslator;
import com.companyz.zplatform.utils.EmailValidator;
import com.companyz.zplatform.utils.PasswordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * Ajayi Segun on 10th December 2022
 */

@Slf4j
@Service
public class UsersServiceImpl implements UsersService {

    //private fields
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    LoginServiceImpl loginService;
    @Autowired
    EmailValidator emailValidator;
    @Autowired
    PasswordValidator passwordValidator;
    @Autowired
    SignUpToUserTranslator signUpToUserTranslator;

    @Override
    public Users register(SignUpDTO newUser) throws InvalidInputException, RecordExistException, GeneralFailureException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        log.info("Register New User");

        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || newUser.getFirstName() == null || newUser.getFirstName().isBlank() || newUser.getLastName() == null ||
                newUser.getLastName().isBlank() || newUser.getGender() == null || newUser.getGender().isBlank() || newUser.getNationality() == null || newUser.getNationality().isBlank() ||
                newUser.getPassword() == null || newUser.getPassword().isBlank()) throw new InvalidInputException("Please fill all required fields");

        log.info("Validating the email");
        if (!emailValidator.validate(newUser.getEmail())) throw new InvalidInputException("Invalid Email");

        log.info("Validating the password");
        if (!passwordValidator.validate(newUser.getPassword())) throw new InvalidInputException("Invalid Password");

        log.info("Checking if email {} exists" , newUser.getEmail());
        Users checkEmailUser = usersRepository.findByEmailIgnoreCaseAndDeleteFlag(newUser.getEmail(), 0);
        if (checkEmailUser != null) throw new RecordExistException("User Already Exist");

        log.info("Converting User From DTO");
        Users user = signUpToUserTranslator.convertSignUpDTOToUser(newUser);

        Login login = new Login();
        login.setUsername(user.getEmail());
        login.setPassword(newUser.getPassword());
        loginService.create(login);

        try {
            return usersRepository.save(user);
        } catch (Exception e) {
            log.error("Error While Registering User {0}" , e);
            throw new GeneralFailureException("Registering User Failed");
        }
    }

    @Override
    public Users uploadVerificationDocuments(VerificationDTO verificationDTO) throws InvalidInputException, RecordNotFoundException, GeneralFailureException {
        log.info("Uploading Verification Documents");

        if (verificationDTO.getUserId() == null || verificationDTO.getUserId().isBlank()) throw new InvalidInputException("Please fill all required fields");

        Optional<Users> user = usersRepository.findById(verificationDTO.getUserId());
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");

        user.get().setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        Users.Identification identification = new Users.Identification();
        identification.setDocument(verificationDTO.getDocument());
        identification.setType(verificationDTO.getType());
        identification.setNumber(verificationDTO.getNumber());
        user.get().setIdentificationDetails(identification);

        try {
            return usersRepository.save(user.get());
        } catch (Exception e) {
            log.error("Error While Uploading Verification Documents {0}" , e);
            throw new GeneralFailureException("Uploading Verification Documents Failed");
        }
    }

    @Override
    public Users verify(String userId, VerificationStatus status) throws GeneralFailureException, InvalidInputException, RecordNotFoundException {
        log.info("Verifying User");

        if (userId == null || userId.isBlank()) throw new InvalidInputException("Please fill all required fields");

        Optional<Users> user = usersRepository.findById(userId);
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");

        user.get().setVerificationStatus(status);

        try {
            return usersRepository.save(user.get());
        } catch (Exception e) {
            log.error("Error While Verifying User {0}" , e);
            throw new GeneralFailureException("Verifying User Failed");
        }
    }

    @Override
    public ResponseDTO checkVerification(String userId) throws RecordNotFoundException, UnverifiedException {
        log.info("Check Verification Status As A Callback");

        Optional<Users> user = usersRepository.findById(userId);
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");
        if (user.get().getVerificationStatus() != VerificationStatus.VERIFIED) throw new UnverifiedException("Not Yet Verified");

        return new ResponseDTO(Constants.SUCCESS, "User Verified Successfully", user.get().getId());
    }

    @Override
    public Users get(String userId) throws RecordNotFoundException {
        log.info("Getting User");

        Optional<Users> user = usersRepository.findById(userId);
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");
        return user.get();
    }

}
