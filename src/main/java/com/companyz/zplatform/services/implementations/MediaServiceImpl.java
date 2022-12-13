package com.companyz.zplatform.services.implementations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.companyz.zplatform.dtos.UploadMediaDTO;
import com.companyz.zplatform.entities.Media;
import com.companyz.zplatform.environment.APIs;
import com.companyz.zplatform.exceptions.InvalidInputException;
import com.companyz.zplatform.exceptions.RecordExistException;
import com.companyz.zplatform.exceptions.RecordNotFoundException;
import com.companyz.zplatform.repositories.MediaRepository;
import com.companyz.zplatform.services.MediaService;
import com.companyz.zplatform.utils.FileConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Ajayi Segun on 22nd April 2022
 */
@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    //private fields
    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    AmazonS3 amazonS3;
    @Autowired
    APIs apis;
    @Autowired
    FileConverter fileConverter;

    @Override
    public Media save(UploadMediaDTO mediaDTO) throws InvalidInputException, RecordExistException, IOException {
        log.info("Uploading Media Document");

        //Validation
        if(mediaDTO.getKey() == null || mediaDTO.getKey().isBlank() || mediaDTO.getType() == null || mediaDTO.getType().isBlank())
            throw new InvalidInputException("Some Required Fields Are Missing");

        Media media = mediaRepository.findByKeyAndDeleteFlag(mediaDTO.getKey(), 0);
        if(media != null) throw new RecordExistException("Record Already Exists");

        File file;
        if(mediaDTO.getMultipartFile() != null) file = fileConverter.convertMultiPartFileToFile(mediaDTO.getMultipartFile());
        else {
            if(mediaDTO.getFile() != null) file = mediaDTO.getFile();
            else throw new InvalidInputException("File Is Missing");
        }

        String displayName = file.getName();
        final String fileName = file.getName();
        log.info("Uploading file with name {}", fileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(apis.getS3BucketName(), fileName, file);
        amazonS3.putObject(putObjectRequest);
        Files.delete(file.toPath()); // Remove the file locally created in the project folder

        //Saving to database
        Media newMedia = new Media();
        newMedia.setDeleteFlag(0);
        newMedia.setCreatedTime(Instant.now());
        newMedia.setDisplayName(displayName);
        newMedia.setKey(mediaDTO.getKey());
        newMedia.setName(fileName);
        newMedia.setType(mediaDTO.getType());
        mediaRepository.save(newMedia);

        return mediaRepository.findByKeyAndDeleteFlag(mediaDTO.getKey(), 0);
    }

    @Override
    public List<Media> gets() {
        log.info("Getting All Media");

        return mediaRepository.findByDeleteFlag(0);
    }

    @Override
    public List<Media> getByIds(List<String> ids) {
        log.info("Getting Media By ID");

        return mediaRepository.findByIdIn(ids);
    }

    @Override
    public Media getByKey(String key) {
        log.info("Getting Media By Key");

        return mediaRepository.findByKeyAndDeleteFlag(key, 0);
    }

    @Override
    public S3ObjectInputStream find(String fileName) {
        log.info("Downloading file");

        return amazonS3.getObject(apis.getS3BucketName(), fileName).getObjectContent();
    }

    @Override
    public List<String> getS3Urls(List<String> fileNames) {
        log.info("Getting file URLs");

        List<String> urlList = new ArrayList<>();
        List<Media> media = mediaRepository.findByNameIn(fileNames);
        if (media.size() <= 0) return urlList;

        for (String fileName : fileNames){
            Media myMedia = mediaRepository.findByNameAndDeleteFlag(fileName, 0);
            urlList.add(amazonS3.getUrl(apis.getS3BucketName(), myMedia.getName()).toExternalForm());
        }

        return urlList;
    }

    @Override
    public void delete(String key) throws RecordNotFoundException {
        log.info("Deleting Media By Key");

        Media media = mediaRepository.findByKeyAndDeleteFlag(key, 0);
        if (media == null) throw new RecordNotFoundException("Record Not Found");

        amazonS3.deleteObject(apis.getS3BucketName(), media.getName());
        media.setDeleteFlag(1);
        mediaRepository.save(media);
    }
}
