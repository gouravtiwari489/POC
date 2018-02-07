package com.datagenerator.demo.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.TableStructureExtractor;

@RestController
@RequestMapping("/getFKRelation")
public class SQLFileReadController extends BaseController{
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	@GetMapping
	public ResponseEntity<List<GenerateDataObject>> getFKRelation() throws Exception {
		List<GenerateDataObject> tableList = new ArrayList<>();
		tableList = tableStructureExtractor.getFKMap();
		return new ResponseEntity<>(tableList, HttpStatus.OK);
	}

}
