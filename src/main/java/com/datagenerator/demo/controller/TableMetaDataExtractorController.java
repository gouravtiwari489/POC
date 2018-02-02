package com.datagenerator.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.utils.FindWordMatchingPossibilities;
import com.datagenerator.demo.utils.TableStructureExtractor;

@RestController
@RequestMapping("/extractor")
public class TableMetaDataExtractorController {

	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	//@Autowired
	//sprivate XmiTableStructureExtractor XmiTableStructureExtractor;
	
	@Autowired
	private FindWordMatchingPossibilities findWordMatchingPossibilities;
	
	@GetMapping
	public ResponseEntity<LinkedHashMap<String, LinkedHashMap<String,String>>> extractTableMetaData() throws ClassNotFoundException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("Dump20180122.sql").getFile());
		LinkedHashMap<String, LinkedHashMap<String,String>> tableMap = tableStructureExtractor.searchforTableName(file);
		return new ResponseEntity<LinkedHashMap<String, LinkedHashMap<String,String>>>(tableMap, HttpStatus.OK);
	}
	
	@PostMapping("/findMatch")
	public ResponseEntity<Map<String,List<String>>> matchWords(@RequestParam(name = "wordToFind", required = true) String wordToFind) throws ClassNotFoundException, IOException {
		Map<String,List<String>> matchingMap = findWordMatchingPossibilities.findMatchingWord(wordToFind);
		return new ResponseEntity<Map<String,List<String>>>(matchingMap, HttpStatus.OK);
	}	
}
