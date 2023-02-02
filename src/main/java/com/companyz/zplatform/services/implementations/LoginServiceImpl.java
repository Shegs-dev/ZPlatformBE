package com.companyz.zplatform.services.implementations;

import com.companyz.zplatform.dtos.*;
import com.companyz.zplatform.entities.Login;
import com.companyz.zplatform.entities.Users;
import com.companyz.zplatform.environment.APIs;
import com.companyz.zplatform.environment.Constants;
import com.companyz.zplatform.exceptions.*;
import com.companyz.zplatform.repositories.LoginRepository;
import com.companyz.zplatform.repositories.UsersRepository;
import com.companyz.zplatform.services.LoginService;
import com.companyz.zplatform.utils.MessageTemplates;
import com.companyz.zplatform.utils.PasswordValidator;
import com.companyz.zplatform.utils.TokenGenerator;
import com.companyz.zplatform.utils.HashPassword;
import com.mailjet.client.errors.MailjetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Ajayi Segun on 10th December 2022
 */

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    //private fields
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    EmailSenderServiceImpl emailSenderService;
    @Autowired
    HashPassword hashPassword;
    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    PasswordValidator passwordValidator;
    @Autowired
    MessageTemplates template;
    @Autowired
    APIs apis;

    @Override
    public ResponseDTO login(LoginDTO loginDTO) throws InvalidInputException, RecordNotFoundException,
            NoSuchAlgorithmException, InvalidKeySpecException, CredentialsIncorrectException, GeneralFailureException {
        log.info("Logging In User");

        if (loginDTO.getUsername() == null || loginDTO.getUsername().isBlank() || loginDTO.getPassword() == null || loginDTO.getPassword().isBlank())
            throw new InvalidInputException("Please enter required fields");

        log.info("Checking User and Password {}", loginDTO);
        Login login = loginRepository.findByUsernameIgnoreCaseAndDeleteFlag(loginDTO.getUsername(), 0);
        log.info("Login {} " , login);
        if (login == null) throw new RecordNotFoundException("User Does Not Exist");
        if (!hashPassword.validatePass(loginDTO.getPassword(), login.getPassword())) throw new CredentialsIncorrectException("Password Incorrect");

        log.info("Generating 2FA Token and Sending Email for Authentication");
        Users user = usersRepository.findByEmailIgnoreCaseAndDeleteFlag(login.getUsername(), 0);
        String token = tokenGenerator.generate();
        login.setToken(token);
        login.setTokenExpires(this.getTokenExpireTime());

        try {
            loginRepository.save(login);

            //Sending Email
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setFname(user.getFirstName());
            messageDTO.setDummyPassword(token);
            MailDTO mailDTO = new MailDTO();
            mailDTO.setMsg(template.getTemplate(3, messageDTO));
            mailDTO.setSubject("ZPlatform - Two Factor Authentication");
            mailDTO.setTo(login.getUsername());
            mailDTO.setFrom(apis.getNoReply());
            mailDTO.setReceiverName(user.getFirstName());

            emailSenderService.sendSimpleEmail(mailDTO);

            return new ResponseDTO(Constants.SUCCESS, "Logged In User Successfully", user.getId());
        } catch (Exception e) {
            log.error("Error While Logging In User {0}" , e);
            throw new GeneralFailureException("Logging In User Failed");
        }
    }

    @Override
    public Login create(Login login) throws InvalidInputException, RecordExistException, NoSuchAlgorithmException, InvalidKeySpecException, GeneralFailureException {
        log.info("Creating Login");

        if (login.getPassword() == null || login.getPassword().isBlank() || login.getUsername() == null || login.getUsername().isBlank())
            throw new InvalidInputException("Please Input required fields");

        Login dbLogin = loginRepository.findByUsernameIgnoreCaseAndDeleteFlag(login.getUsername(), 0);
        if (dbLogin != null) throw new RecordExistException("Login Already Exist");

        login.setCreatedTime(Instant.now());
        login.setDeleteFlag(0);
        login.setPassword(hashPassword.hashPass(login.getPassword()));

        try {
            return loginRepository.save(login);
        } catch (Exception e) {
            log.error("Error While Creating Login {0}" , e);
            throw new GeneralFailureException("Creating Login Failed");
        }
    }

    @Override
    public Users authenticateLogin(AuthenticateLoginDTO authenticateLoginDTO) throws InvalidInputException, RecordNotFoundException,
            CredentialsIncorrectException, AccessExpiredException {
        log.info("Authenticating Login");

        if (authenticateLoginDTO.getToken() == null || authenticateLoginDTO.getToken().isBlank() || authenticateLoginDTO.getUserId() == null ||
                authenticateLoginDTO.getUserId().isBlank()) throw new InvalidInputException("Please enter required fields");

        Optional<Users> user = usersRepository.findById(authenticateLoginDTO.getUserId());
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");

        log.info("Checking the token sent");
        Login login = loginRepository.findByUsernameIgnoreCaseAndDeleteFlag(user.get().getEmail(), 0);
        if (!authenticateLoginDTO.getToken().equals(login.getToken())) throw new CredentialsIncorrectException("Invalid OTP");
        if (Instant.now().isAfter(login.getTokenExpires())) throw new AccessExpiredException("OTP Expired");

        return user.get();
    }

    @Override
    public ResponseDTO initiateResetPassword(ResetPasswordDTO resetPasswordDTO) throws InvalidInputException, RecordNotFoundException,
            DuplicateRequestException, GeneralFailureException {
        log.info("Initiating Reset Password");

        if (resetPasswordDTO.getEmail() == null || resetPasswordDTO.getEmail().isBlank()) throw new InvalidInputException("Please enter required field");

        Login login = loginRepository.findByUsernameIgnoreCaseAndDeleteFlag(resetPasswordDTO.getEmail(), 0);
        if (login == null) throw new RecordNotFoundException("User Does Not Exist");
        if (login.isResetActive()) throw new DuplicateRequestException("Reset Request Already Sent Before");

        log.info("Updating Login Reset and Old Passwords History");
        login.setResetActive(true);
        List<String> oldPasswords = new ArrayList<>();
        if (login.getOldPasswords() != null) oldPasswords = login.getOldPasswords();
        oldPasswords.add(login.getPassword());
        login.setOldPasswords(oldPasswords);

        try {
            loginRepository.save(login);

            log.info("Sending Reset Password Email");
            Users user = usersRepository.findByEmailIgnoreCaseAndDeleteFlag(login.getUsername(), 0);
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setFname(user.getFirstName());
            messageDTO.setUrl(apis.getZplatformUrl() + apis.getResetPasswordUrl());
            messageDTO.setUserId(user.getId());
            MailDTO mailDTO = new MailDTO();
            mailDTO.setMsg(template.getTemplate(2, messageDTO));
            mailDTO.setSubject("ZPlatform - Reset Password");
            mailDTO.setTo(login.getUsername());
            mailDTO.setFrom(apis.getNoReply());
            mailDTO.setReceiverName(user.getFirstName());
            emailSenderService.sendSimpleEmail(mailDTO);

            return new ResponseDTO(Constants.SUCCESS, "Initiated Password Reset Successfully", user.getId());
        } catch (Exception e) {
            log.error("Error While Initiating Password Reset {0}" , e);
            throw new GeneralFailureException("Initiating Password Reset Failed");
        }
    }

    @Override
    public ResponseDTO completeResetPassword(LoginDTO loginDTO) throws InvalidInputException, RecordNotFoundException, NoSuchAlgorithmException,
            InvalidKeySpecException, GeneralFailureException {
        log.info("Completing Reset Password");

        if (loginDTO.getPassword() == null || loginDTO.getPassword().isBlank() || loginDTO.getUsername() == null || loginDTO.getPassword().isBlank())
            throw new InvalidInputException("Please enter required fields");

        log.info("Validating Reset");
        if (!passwordValidator.validate(loginDTO.getPassword())) throw new InvalidInputException("Invalid Password");
        Optional<Users> user = usersRepository.findById(loginDTO.getUsername());
        if (user.isEmpty()) throw new RecordNotFoundException("User Does Not Exist");
        Login login = loginRepository.findByUsernameIgnoreCaseAndDeleteFlag(user.get().getEmail(), 0);
        if (login == null) throw new RecordNotFoundException("User Does Not Exist");
        List<String> oldPasswords = new ArrayList<>();
        if (login.getOldPasswords() != null) oldPasswords = login.getOldPasswords();
        for (String oldPassword : oldPasswords){
            if (hashPassword.validatePass(loginDTO.getPassword(), oldPassword)) throw new InvalidInputException("Password Already Used Before");
        }

        login.setPassword(hashPassword.hashPass(loginDTO.getPassword()));
        login.setResetActive(false);

        try {
            loginRepository.save(login);

            return new ResponseDTO(Constants.SUCCESS, "Validated Password Reset Successfully", user.get().getId());
        } catch (Exception e) {
            log.error("Error While Validating Password Reset {0}" , e);
            throw new GeneralFailureException("Validating Password Reset Failed");
        }
    }

    //Method to get time that token expires
    private Instant getTokenExpireTime(){
        Instant now = Instant.now();
        return now.plus(5, ChronoUnit.MINUTES);
    }
}
