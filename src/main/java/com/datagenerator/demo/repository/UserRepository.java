package com.datagenerator.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.Repository;

import com.datagenerator.demo.domain.User;


public interface UserRepository extends MongoRepository<User, Integer> {

		User findByUsername(String username);
}