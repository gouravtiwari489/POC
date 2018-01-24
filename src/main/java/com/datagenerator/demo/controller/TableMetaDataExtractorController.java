package com.datagenerator.demo.controller;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
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
	public ResponseEntity<LinkedHashMap<String, LinkedHashMap<String,String>>> extractTableMetaData() throws FileNotFoundException {
		LinkedHashMap<String, LinkedHashMap<String,String>> tableMap = tableStructureExtractor.searchforTableName();
		return new ResponseEntity<LinkedHashMap<String, LinkedHashMap<String,String>>>(tableMap, HttpStatus.OK);
	}
}
