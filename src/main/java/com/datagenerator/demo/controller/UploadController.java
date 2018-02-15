package com.datagenerator.demo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
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
import com.datagenerator.demo.serviceImpl.DataGenerationService;
import com.datagenerator.demo.utils.CustomTokenConverter;

@RestController
public class UploadController {


	@Autowired
	SQLFileReadService sqlFileReadService;

	@Autowired
	DataGenerationService dataGenerationService;

	@Autowired
	private CustomTokenConverter customTokenConverter;

	@Autowired
	private HttpServletResponse response;

	@PostMapping("/upload")
	public ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>> uploadProfile(
			@RequestParam(name = "file", required = true) MultipartFile multipartFile,
			@RequestParam(name = "domainType", required = true) String domainType) throws Exception {
		/*
		 * if (null == multipartFile) { return new
		 * ResponseEntity<String>("Please select file for upload.!",
		 * HttpStatus.BAD_REQUEST); }
		 */
		// uploadService.uploadFile(multipartFile);
		List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = sqlFileReadService.readSQLfile(multipartFile,
				domainType);
		customTokenConverter.setAdditionalInfo("mappedTables", list);
		return new ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>>(list, HttpStatus.OK);
	}

	@GetMapping("/download")
	public ResponseEntity<?> downloadFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("Dump20180122.sql").getFile());
		InputStreamSource resource = null;
		try {
			resource = new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length()).body(resource);
	}

	@PostMapping("/downloadExcel")
	public ResponseEntity<?> downloadExcelFile(@RequestParam(name = "fileType", required = true) String fileType) throws Exception {
		System.out.println("@@@@@@@@@@@@@@@@@   "+fileType+"   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		dataGenerationService.generateData();
		Resource resource = new ClassPathResource("output/ExcelSheet.xls");
		File file = resource.getFile();
		BufferedInputStream isr = new BufferedInputStream(new FileInputStream(file));
		if (file != null) {
			response.setContentType("application/vnd.ms-excel");
			response.setContentLength((int) file.length());
			response.setHeader("filename", file.getName());
			IOUtils.copyLarge(isr, response.getOutputStream());
		} else {
			isr.close();
			throw new Exception("Excel file not found");
		}
		return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
	}

}
