package com.companyz.zplatform.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Ajayi Segun on 22nd April 2022
 */
@Data
@Document
public class Media {

    //private fields
    @Id
    private String id;
    private String name;
    private String displayName;
    private String key;
    private String type;
    private Instant createdTime;
    private int deleteFlag;
}
