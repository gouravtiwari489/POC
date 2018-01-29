package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.domain.Domain;
import com.datagenerator.demo.repository.DomainRepository;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.TableStructureExtractor;

@Service
public class SQLFileReadServiceImpl implements SQLFileReadService{

	@Autowired
	DomainRepository  domainRepository;
	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	public void readSQLfile(MultipartFile multiFile) throws Exception {
		File convFile = new File(multiFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multiFile.getBytes());
		LinkedHashMap<String, LinkedHashMap<String,String>> inputTableMap = tableStructureExtractor.searchforTableName(convFile);
		System.out.println("inputTableMap--"+inputTableMap);
		Map<String, LinkedHashMap<String, String>> finalInputMap = new LinkedHashMap<>();
		Map<String, LinkedHashMap<String, String>> finalMappedMap = new LinkedHashMap<>();
		List<Domain>  domains = domainRepository.getDomainByDomainId("HRMS");
		List<Object> tables = domains.get(0).getTables();
		Map<String, LinkedHashMap<String, String>> availableTables = new LinkedHashMap<>();
		for(Object table:tables){
			 Map<String, LinkedHashMap<String, String>> tbl = new LinkedHashMap<>();
			 tbl  = (Map<String, LinkedHashMap<String, String>>) table;
			 for (Map.Entry<String, LinkedHashMap<String,String>> entry : tbl.entrySet()) {
				 availableTables.put(entry.getKey(), entry.getValue());
			 }
		}
		System.out.println("availableTables2222-----"+availableTables);
		if(inputTableMap !=null && !inputTableMap.isEmpty()){
			for (Map.Entry<String, LinkedHashMap<String,String>> entry : inputTableMap.entrySet()) {
			    String inputTableName = entry.getKey();
			    LinkedHashMap<String,String> inputTableFields = entry.getValue();
			    System.out.println("inputTableName 11: " + inputTableName + " inputTableFields 22: " + inputTableFields);
				for (Map.Entry<String, LinkedHashMap<String,String>> availablemap : availableTables.entrySet()) {
					boolean tableMatched = false;
					boolean fieldsMatched = false;
					String tablekey = availablemap.getKey();
					LinkedHashMap<String,String> fieldsvalue = availablemap.getValue();
				    System.out.println("available table 33: " + tablekey + " available columns 44: " + fieldsvalue);
				    if(tablekey.contains(inputTableName)){
				    	tableMatched=  true;
				    }
				     int counter = 0;
				     int inputTableFieldsSize = inputTableFields.keySet().size();
				    for (String inputColumnName : inputTableFields.keySet()) {
				    	for(String availableColumn : fieldsvalue.keySet()){
				    		if(inputColumnName.contains("PK") || inputColumnName.contains("FK")){
				    			inputTableFieldsSize--;
				    		}
				    		if(availableColumn.contains(inputColumnName)){
				    			counter++;
				    		}
				    	}
				    }
				    System.out.println("counter size--"+counter);
				    System.out.println("inputTableFieldsSize size--"+inputTableFieldsSize);
					if (counter > 0 && inputTableFieldsSize > 0) {
						int percentage = (counter / inputTableFieldsSize) * 100;
						if (percentage >= 80)
							fieldsMatched = true;
					}
				    System.out.println("tableMatched : " + tableMatched + " fieldsMatched : " + fieldsMatched);
				    System.out.println("final input table : " + inputTableName + " final input columns : " + inputTableFields);
				    System.out.println("final available table : " + tablekey + " final available columns : " + fieldsvalue);
				    finalInputMap.put(inputTableName, inputTableFields);
				    if(tableMatched && fieldsMatched){
				    	finalMappedMap.put(tablekey, fieldsvalue);
				    }
				}
			}
		}
		System.out.println("final input---"+finalInputMap.toString());
		System.out.println("final available---"+finalMappedMap.toString());
		fos.close();
	}
	
}
