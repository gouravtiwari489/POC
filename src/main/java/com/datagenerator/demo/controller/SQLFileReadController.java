/*package com.datagenerator.demo.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.service.SQLFileReadService;

@RestController
@RequestMapping("/uploadfile")
public class SQLFileReadController extends BaseController{
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@PostMapping
	public ResponseEntity<String> createParser(@RequestParam(name="file") MultipartFile multipartFile) throws Exception {
		sqlFileReadService.readSQLfile(multipartFile);
		
		return new ResponseEntity<>("uploaded successfully", HttpStatus.OK);
	}

}
*/