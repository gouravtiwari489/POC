package com.datagenerator.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.domain.User;


@RestController
@CrossOrigin
public class LoginController {
	
	@PostMapping("/login")
	public ResponseEntity<HttpStatus> getAllEmps(@RequestBody User user){
		
		if(user.getUserName().equalsIgnoreCase("admin") && user.getPassword().equalsIgnoreCase("admin")){
			return new  ResponseEntity<>(HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
