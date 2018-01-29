package com.datagenerator.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.serviceImpl.UploadService;


@RestController
public class UploadController {
	
	@Autowired
	private	UploadService uploadService;
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadProfile(@RequestParam(name = "file", required = true) MultipartFile multipartFile) throws Exception {
		if (null == multipartFile) {
			return new ResponseEntity<String>("Please select file for upload.!", HttpStatus.BAD_REQUEST);
		}
		uploadService.uploadFile(multipartFile);
		sqlFileReadService.readSQLfile(multipartFile);
		return new ResponseEntity<String>("file uploaded successfully.!", HttpStatus.OK);
	}
	
}
