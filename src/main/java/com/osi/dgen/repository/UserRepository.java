package com.osi.dgen.repository;

import com.osi.dgen.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

  User findByUsername(String username);
}
