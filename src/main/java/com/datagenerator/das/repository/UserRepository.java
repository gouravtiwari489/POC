package com.datagenerator.das.repository;

import com.datagenerator.das.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

  User findByUsername(String username);
}
