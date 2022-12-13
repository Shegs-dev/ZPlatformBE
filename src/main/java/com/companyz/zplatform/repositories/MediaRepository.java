package com.companyz.zplatform.repositories;

import com.companyz.zplatform.entities.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ajayi Segun on 22nd April 2022
 */
@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
    Media findByKeyAndDeleteFlag(String key, int deleteFlag);

    List<Media> findByDeleteFlag(int deleteFlag);

    List<Media> findByIdIn(List<String> ids);

    List<Media> findByNameIn(List<String> fileNames);

    Media findByNameAndDeleteFlag(String fileName, int deleteFlag);
}
