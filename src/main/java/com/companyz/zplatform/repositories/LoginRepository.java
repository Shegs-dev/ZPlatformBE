package com.companyz.zplatform.repositories;

import com.companyz.zplatform.entities.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Repository
public interface LoginRepository extends MongoRepository<Login, String> {
    Login findByUsernameIgnoreCaseAndDeleteFlag(String username, int deleteFlag);
}
