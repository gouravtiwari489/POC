package com.datagenerator.demo.controller;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.CustomTokenConverter;
import com.datagenerator.demo.utils.TableStructureExtractor;

@Scope("request")
@RestController
@RequestMapping("/getFKRelation")
public class SQLFileReadController extends BaseController{
	
	@Autowired
	SQLFileReadService sqlFileReadService;
	
	@Autowired
	private CustomTokenConverter customTokenConverter;
	
	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	@GetMapping
	public ResponseEntity<List<GenerateDataObject>> getFKRelation() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("mysqlsampledatabase.sql").getFile());
		List<GenerateDataObject> tableList = tableStructureExtractor.getFKMap(file);
		customTokenConverter.setAdditionalInfo("getFKRelation",tableList);
		return new ResponseEntity<>(tableList, HttpStatus.OK);
	}

}
