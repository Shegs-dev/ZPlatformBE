package com.companyz.zplatform.repositories;

import com.companyz.zplatform.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author OluwasegunAjayi on 9th December 2022
 *
 */

@Repository
public interface UsersRepository extends MongoRepository<Users, String> {
    Users findByEmailIgnoreCaseAndDeleteFlag(String email, int deleteFlag);
}
