package com.osi.datagen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.osi.datagen.domain.User;

public interface UserRepository extends MongoRepository<User, Integer> {

  User findByUsername(String username);
}
