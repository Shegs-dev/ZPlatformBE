package com.companyz.zplatform.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

/** Ajayi Segun on 17th March 2022 **/

@Data
@AllArgsConstructor
public class ErrorDetails {

    //private fields
    private Instant timestamp;
    private String message;
    private String details;
}
