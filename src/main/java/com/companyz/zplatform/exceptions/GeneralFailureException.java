package com.companyz.zplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ajayi Segun on 13th January 2023
 */

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class GeneralFailureException extends Exception {

    private static final long serialVersionUID = 1L;

    public GeneralFailureException(String message){
        super(message);
    }
}
