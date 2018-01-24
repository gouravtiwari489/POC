package com.datagenerator.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.service.SQLFileReadService;

@RestController
@RequestMapping("/uploadfile")
public class SQLFileReadController extends BaseController{
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@PostMapping
	public ResponseEntity<String> createParser() throws Exception {
		sqlFileReadService.readSQLfile();
		return new ResponseEntity<>("uploaded successfully", HttpStatus.OK);
	}

}
