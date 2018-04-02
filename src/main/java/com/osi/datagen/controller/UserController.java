package com.osi.datagen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osi.datagen.intf.IUserService;
import com.osi.datagen.domain.ResponseMessageVO;
import com.osi.datagen.domain.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	IUserService userService;

	@PostMapping("/users")
	public ResponseEntity<?> createUser(@RequestBody User user){
		User userEmailIDExists = userService.findByEmailId(user.getEmailId());
		if (userEmailIDExists != null) {
			System.out.println("A emailId with " + user.getEmailId() + " already exist");
			return new ResponseEntity<>((user.getEmailId()+" already exist"),HttpStatus.CONFLICT);
		}
		User userInfo = userService.createUser(user);
		log.info(userInfo.getUsername()+" user created successfully"); 
		return new ResponseEntity<>(("Successfully registered"),HttpStatus.CREATED);
	}
}
