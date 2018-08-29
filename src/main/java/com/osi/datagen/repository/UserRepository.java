package com.osi.datagen.repository;

import com.osi.datagen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

  User save(User user);

  /*User findByEmailId(String emailId);*/
}
