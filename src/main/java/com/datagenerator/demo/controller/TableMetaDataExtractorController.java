package com.datagenerator.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.utils.FindWordMatchingPossibilities;
import com.datagenerator.demo.utils.Generator;
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
		File file = new File("C:\\Users\\dthorat\\Downloads\\Dump20180122.sql");
		LinkedHashMap<String, LinkedHashMap<String,String>> tableMap = tableStructureExtractor.searchforTableName(file);
		Generator generator = new Generator("src\\main\\java", "com\\datagenerator\\demo\\domain", "", null, null, true);
        System.out.println("Generating classes....");
        Set set = tableMap.entrySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Entry<String, Map> entry = (Entry<String, Map>) itr.next();
			generator.generateClasses(entry.getKey(), entry.getValue());
		}	
		System.out.println(String.format("Generated %s files. All done.", generator.getGeneratedFileCount()));
		//XmiTableStructureExtractor.searchforTableName();
		//return new ResponseEntity<LinkedHashMap<String, LinkedHashMap<String,String>>>(tableMap, HttpStatus.OK);
		return null;
	}
	
	@PostMapping("/findMatch")
	public ResponseEntity<Map<String,String>> matchWords(@RequestParam(name = "wordToFind", required = true) String wordToFind) throws ClassNotFoundException, IOException {
		Map<String,String> matchingMap = findWordMatchingPossibilities.findMatchingWord(wordToFind);
		return new ResponseEntity<Map<String,String>>(matchingMap, HttpStatus.OK);
	}	
}
