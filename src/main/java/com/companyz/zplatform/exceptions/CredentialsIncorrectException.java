package com.companyz.zplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Ajayi Segun on 11th December 2022 **/

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class CredentialsIncorrectException extends Exception {

    private static final long serialVersionUID = 1L;

    public CredentialsIncorrectException(String message){
        super(message);
    }
}
