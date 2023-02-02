package com.companyz.zplatform.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author OluwasegunAjayi on 13th December 2022
 *
 */

@Data
@AllArgsConstructor
public class ResponseDTO {

    //private fields
    private String status;
    private String message;
    private Object data;
}
