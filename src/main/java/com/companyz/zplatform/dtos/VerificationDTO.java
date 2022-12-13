package com.companyz.zplatform.dtos;

import com.companyz.zplatform.entities.Media;
import lombok.Data;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Data
public class VerificationDTO {

    //private fields
    private String userId;
    private String type;//NIN (National Identification Number) or PN (Passport Number)
    private String number;
    private Media document;
}
