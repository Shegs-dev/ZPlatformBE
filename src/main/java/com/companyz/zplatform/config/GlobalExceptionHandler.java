package com.companyz.zplatform.config;

import com.companyz.zplatform.errors.ErrorDetails;
import com.companyz.zplatform.exceptions.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

/** Ajayi Segun on 17th March 2022 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> resourceNotFoundException(UnauthorizedAccessException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
