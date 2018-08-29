package com.poc.gourav.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.gourav.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

  User save(User user);

  /*User findByEmailId(String emailId);*/
}
