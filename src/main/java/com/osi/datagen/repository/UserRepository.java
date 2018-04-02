package com.osi.datagen.repository;

import com.osi.datagen.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

  User findByUsername(String username);
  User save(User user);
  User findByEmailId(String emailId);
  
  
}
