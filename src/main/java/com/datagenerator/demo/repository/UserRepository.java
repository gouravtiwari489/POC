package com.datagenerator.demo.repository;

import com.datagenerator.demo.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

  User findByUsername(String username);
}
