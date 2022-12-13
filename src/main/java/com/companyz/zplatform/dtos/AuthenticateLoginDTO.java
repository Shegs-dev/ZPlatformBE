package com.companyz.zplatform.dtos;

import lombok.Data;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Data
public class AuthenticateLoginDTO {

    //private fields
    private String userId;
    private String token;
}
