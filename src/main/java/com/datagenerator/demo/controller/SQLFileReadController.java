package com.datagenerator.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.TableStructureExtractor;

@RestController
@RequestMapping("/getFKRelation")
public class SQLFileReadController extends BaseController{
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	@PostMapping
	public ResponseEntity<LinkedHashMap<String, List<String>>> getFKRelation() throws Exception {
		LinkedHashMap<String, List<String>> fkMap = new LinkedHashMap<>();
		fkMap = tableStructureExtractor.getFKMap();
		return new ResponseEntity<>(fkMap, HttpStatus.OK);
	}

}
