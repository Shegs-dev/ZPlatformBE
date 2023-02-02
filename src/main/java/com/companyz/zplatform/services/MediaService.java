package com.companyz.zplatform.services;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.companyz.zplatform.dtos.ResponseDTO;
import com.companyz.zplatform.dtos.UploadMediaDTO;
import com.companyz.zplatform.entities.Media;
import com.companyz.zplatform.exceptions.GeneralFailureException;
import com.companyz.zplatform.exceptions.InvalidInputException;
import com.companyz.zplatform.exceptions.RecordExistException;
import com.companyz.zplatform.exceptions.RecordNotFoundException;

import java.util.List;

/**
 * Ajayi Segun on 22nd April 2022
 */
public interface MediaService {

    //Service Methods
    ResponseDTO save(UploadMediaDTO mediaDTO) throws InvalidInputException, GeneralFailureException, RecordExistException;
    List<Media> gets();
    List<Media> getByIds(List<String> ids);
    Media getByKey(String key);
    S3ObjectInputStream find(String fileName);
    List<String> getS3Urls(List<String> fileNames);
    ResponseDTO delete(String key) throws GeneralFailureException, RecordNotFoundException;
}
