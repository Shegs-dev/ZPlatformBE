package com.companyz.zplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Ajayi Segun on 9th December 2022 **/

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class RecordNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(String message){
        super(message);
    }
}
