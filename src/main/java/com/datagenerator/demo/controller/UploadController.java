package com.datagenerator.demo.controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.serviceImpl.UploadService;
import com.datagenerator.demo.utils.CustomTokenConverter;


@RestController
public class UploadController {
	
	@Autowired
	private	UploadService uploadService;
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@Autowired
	private CustomTokenConverter customTokenConverter;
	
	@PostMapping("/upload")
	public ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String,String>>>> uploadProfile(@RequestParam(name = "file", required = true) MultipartFile multipartFile,@RequestParam(name = "domainType", required = true)String domainType) throws Exception {
		/*if (null == multipartFile) {
			return new ResponseEntity<String>("Please select file for upload.!", HttpStatus.BAD_REQUEST);
		}*/
		uploadService.uploadFile(multipartFile);
		List<LinkedHashMap<String, LinkedHashMap<String,String>>> list = sqlFileReadService.readSQLfile(multipartFile,domainType);
		customTokenConverter.setAdditionalInfo("upload",list);
		return new ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String,String>>>>(list,HttpStatus.OK);
	}
	
	@GetMapping("/download")
	public ResponseEntity<?> downloadFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("Dump20180122.sql").getFile());
		InputStreamSource resource=null;
		try {
			resource = new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length()).body(resource);
	}
	
}
