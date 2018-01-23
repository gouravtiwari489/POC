package com.datagenerator.demo.controller;

import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.utils.TableStructureExtractor;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/extractor")
@Slf4j
public class TableMetaDataExtractorController {

	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	@GetMapping
	public ResponseEntity<Map<String, Map<String,String>>> extractTableMetaData() throws FileNotFoundException {
		Map<String, Map<String,String>> tableMap = tableStructureExtractor.searchforTableName();
		return new ResponseEntity<Map<String, Map<String,String>>>(tableMap, HttpStatus.OK);
	}
}
