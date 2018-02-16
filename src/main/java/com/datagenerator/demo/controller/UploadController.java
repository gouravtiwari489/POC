package com.datagenerator.demo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.serviceImpl.DataGenerationService;
import com.datagenerator.demo.utils.CustomTokenConverter;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
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
		
		List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = sqlFileReadService.readSQLfile(multipartFile,
				domainType);
		customTokenConverter.setAdditionalInfo("mappedTables", list);
		return new ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>>(list, HttpStatus.OK);
	}



	@PostMapping("/download")
	public ResponseEntity<?> downloadExcelFile(@RequestParam(name = "fileType", required = true) String fileType) throws Exception {
		log.info("@@@@@@@@@@@@@@@@@   "+fileType+"   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		dataGenerationService.generateData();
		String generatedFileName = fileType.equals("xls") ? "output/ExcelSheet.xls" : fileType.equals("csv") ? "output/DAS.zip" : fileType.equals("sql") ? "output/DAS.sql" : "output/DAS.xml";
		String responseContentType = fileType.equals("xls") ? "application/vnd.ms-excel" : fileType.equals("csv") ? "application/csv" : fileType.equals("sql") ? "application/sql" : "application/xml";
		Resource resource = new ClassPathResource(generatedFileName);
		File file = resource.getFile();
		BufferedInputStream isr = new BufferedInputStream(new FileInputStream(file));
		if (file != null) {
			response.setContentType(responseContentType);
			response.setContentLength((int) file.length());
			response.setHeader("filename", file.getName());
			IOUtils.copyLarge(isr, response.getOutputStream());
		} else {
			isr.close();
			log.error("Excel file not found");
			throw new Exception("Excel file not found");
		}
		response.getOutputStream().flush();
		response.getOutputStream().close();
		return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
	}
}
