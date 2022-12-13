package com.companyz.zplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Ajayi Segun on 17th March 2022 **/

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends Exception{

    private static final long serialVersionUID = 1L;

    public UnauthorizedAccessException(String message){
        super(message);
    }
}
