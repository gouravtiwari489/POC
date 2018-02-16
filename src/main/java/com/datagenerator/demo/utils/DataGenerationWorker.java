package com.datagenerator.demo.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.datagenerator.demo.serviceImpl.DataGenerationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {
	
	
    private String tableName;
    private CountDownLatch countDownLatch;
   
   
    
    public DataGenerationWorker(String tableName, CountDownLatch countDownLatch) {
        
        this.tableName = tableName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
    	
    	String test="";
        try {
        	test=doSomeWork(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println(test+"End");
        countDownLatch.countDown();
        
    }
    private String doSomeWork(String tableName) throws Exception {
    	
    	Map<String, String> fieldMap = getMappedFields(tableName);
    	log.info("fieldMap:::::::::::::::::::"+fieldMap+"");
    	Resource resource = new ClassPathResource("output");
    	List<List<String>> excelData=GenerateSampleDataUtil.generateData(fieldMap);
    	GenerateExcelUtil.createAndInsertDataIntoSheet(resource.getFile().getPath()+"\\ExcelSheet.xls", tableName, excelData);
    	//CreateFileAndWrite.insertDataIntoSheet(fieldMap, resource.getFile().getPath()+"//ExcelSheet.xls", tableName);
    	return tableName;
    }
    
    private Map<String, String> getMappedFields(String tableName) {
    	List<LinkedHashMap<String, LinkedHashMap<String, String>>> finalFieldMap = DataGenerationService.tablFieldMappingeMap;
    	
    	for (LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap : finalFieldMap) {
    		if(linkedHashMap.get(tableName)!=null){
    			return linkedHashMap.get(tableName);
    		}else{
    			continue;
    		}
    		
		}
    	return null;
    }
  

}
