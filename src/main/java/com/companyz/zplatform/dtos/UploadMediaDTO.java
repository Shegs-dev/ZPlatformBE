package com.companyz.zplatform.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Ajayi Segun on 22nd April 2022
 */
@Data
public class UploadMediaDTO {

    //private fields
    private MultipartFile multipartFile;
    private File file;
    private String key;
    private String type;
}
