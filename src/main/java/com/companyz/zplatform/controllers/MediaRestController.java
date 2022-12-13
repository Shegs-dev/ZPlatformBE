package com.companyz.zplatform.controllers;

import com.companyz.zplatform.dtos.DownloadMediaDTO;
import com.companyz.zplatform.dtos.UploadMediaDTO;
import com.companyz.zplatform.exceptions.RecordNotFoundException;
import com.companyz.zplatform.services.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Ajayi Segun on 22nd April 2022
 */

@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Media Endpoints", description = "These endpoints manages media on ZPlatform")
@RequestMapping(path = "/media")
@RestController("mediaRestController")
public class MediaRestController {

    //private fields
    @Autowired
    private MediaService mediaService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Operation(description = "This Service uploads media on ZPlatform")
    public ResponseEntity<?> add(@RequestBody UploadMediaDTO mediaDTO, @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                 @RequestParam(name = "key", required = false) String key, @RequestParam(name = "type", required = false) String type) throws Exception {
        log.info("API Call To Upload Media");

        mediaDTO.setMultipartFile(multipartFile);
        if (key != null && !key.isBlank()) mediaDTO.setKey(key);
        if (type != null && !type.isBlank()) mediaDTO.setType(type);

        return ResponseEntity.ok(mediaService.save(mediaDTO));
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @Operation(description = "This Service uploads frontend media on Eko Atlantic")
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                 @RequestParam(name = "key", required = false) String key, @RequestParam(name = "type", required = false) String type) throws Exception {
        log.info("API Call To Upload Media");

        UploadMediaDTO mediaDTO = new UploadMediaDTO();
        mediaDTO.setMultipartFile(multipartFile);
        if (key != null && !key.isBlank()) mediaDTO.setKey(key);
        if (type != null && !type.isBlank()) mediaDTO.setType(type);

        return ResponseEntity.ok(mediaService.save(mediaDTO));
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @Operation(description = "This Service downloads media on Eko Atlantic")
    public ResponseEntity<?> add(@RequestBody DownloadMediaDTO mediaDTO){
        log.info("API Call To Download Media");

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + mediaDTO.getName() + "\"")
                .body(new InputStreamResource(mediaService.find(mediaDTO.getName())));
    }

    @RequestMapping(value = "/gets", method = RequestMethod.GET)
    @Operation(description = "This Service gets organization media")
    public ResponseEntity<List<?>> gets(){
        log.info("API Call To Fetch Organization Media");

        return new ResponseEntity<>(mediaService.gets(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getByIds/{ids}", method = RequestMethod.GET)
    @Operation(description = "This Service get media")
    public ResponseEntity<List<?>> get(@PathVariable List<String> ids){
        log.info("API Call To Fetch Media");

        return new ResponseEntity<>(mediaService.getByIds(ids), HttpStatus.OK);
    }

    @RequestMapping(value = "/getByKey/{key}", method = RequestMethod.GET)
    @Operation(description = "This Service gets organization media by key")
    public ResponseEntity<?> getByKey(@PathVariable String key){
        log.info("API Call To Fetch Organization Media By Key");

        return new ResponseEntity<>(mediaService.getByKey(key), HttpStatus.OK);
    }

    @RequestMapping(value = "/getS3Urls/{fileNames}", method = RequestMethod.GET)
    @Operation(description = "This Service get media S3 Url")
    public ResponseEntity<List<?>> getS3Urls(@PathVariable List<String> fileNames){
        log.info("API Call To Fetch Media S3 Url");

        return new ResponseEntity<>(mediaService.getS3Urls(fileNames), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{key}", method = RequestMethod.DELETE)
    @Operation(description = "This Service deletes a media on Eko-Atlantic")
    public ResponseEntity<?> delete(@PathVariable String key) throws RecordNotFoundException {
        log.info("API Call To Delete A Media");

        mediaService.delete(key);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
