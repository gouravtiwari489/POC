package com.datagenerator.demo.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.serviceImpl.DataGenerationService;

public class Worker implements Runnable {
	
	
    private GenerateDataObject tableDataObject;
    private CountDownLatch countDownLatch;
    
    public Worker(GenerateDataObject tableDataObject, CountDownLatch countDownLatch) {
        
        this.tableDataObject = tableDataObject;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
    	
    	String test="";
        try {
        	test=doSomeWork(tableDataObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println(test+"End");
        countDownLatch.countDown();
        
    }
    private String doSomeWork(GenerateDataObject tableDataObject) throws Exception {
    	
    	Map<String, String> fieldMap = getMappedFields(tableDataObject.getTableName());
    			System.out.println("fieldMap:::::::::::::::::::"+fieldMap+"");
    	Resource resource = new ClassPathResource("output");
    	CreateFileAndWrite.insertDataIntoSheet(fieldMap, resource.getFile().getPath()+"//ExcelSheet.xls", tableDataObject.getTableName());
    	if(tableDataObject.getChildTableName()!=null && tableDataObject.getChildTableName().size()>0)
		{
    		DataGenerationService.threadService(tableDataObject.getChildTableName());
		}
    	return tableDataObject.getTableName();
    }
    
    private Map<String, String> getMappedFields(String tableName) {
    	List<LinkedHashMap<String, LinkedHashMap<String, String>>> finalFieldMap = DataGenerationService.tableMap;
    	
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
