package com.osi.datagen.controller;

import com.osi.datagen.domain.User;
import com.osi.datagen.intf.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

  @Autowired IUserService userService;

  @PostMapping("/users")
  public ResponseEntity<?> createUser(@RequestBody User user) {
    User usernameExists = userService.findByUserName(user.getUsername());
    if (usernameExists != null) {
      System.out.println("A emailId with " + user.getUsername() + " already exist");
      return new ResponseEntity<>((user.getUsername() + " already exist"), HttpStatus.CONFLICT);
    }
    User userInfo = userService.createUser(user);
    log.info(userInfo.getUsername() + " user created successfully");
    return new ResponseEntity<>(("Successfully registered"), HttpStatus.CREATED);
  }
}
