package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.datagenerator.demo.repository.DomainRepository;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.FindWordMatchingPossibilities;
import com.datagenerator.demo.utils.TableStructureExtractor;

@Service
public class SQLFileReadServiceImpl implements SQLFileReadService{

	@Autowired
	DomainRepository  domainRepository;
	@Autowired
	private TableStructureExtractor tableStructureExtractor;
	
	@Autowired
	private FindWordMatchingPossibilities findWordMatchingPossibilities;
	
	@Override
	public List<LinkedHashMap<String, LinkedHashMap<String,String>>> readSQLfile(MultipartFile multiFile,String domainType) throws Exception {
		
		File convFile = new File(multiFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multiFile.getBytes());
		LinkedHashMap<String, LinkedHashMap<String,String>> inputTableMap = tableStructureExtractor.searchforTableName(convFile);
		System.out.println("inputTableMap--"+inputTableMap);
		LinkedHashMap<String, LinkedHashMap<String, String>> finalInputMap = new LinkedHashMap<>();
		LinkedHashMap<String, LinkedHashMap<String, String>> finalMappedMap = new LinkedHashMap<>();
		Map<String, LinkedHashMap<String, String>> availableTables = new LinkedHashMap<>();
		
		System.out.println("availableTables2222-----"+availableTables);
		if(inputTableMap !=null && !inputTableMap.isEmpty()){
			for (Map.Entry<String, LinkedHashMap<String,String>> entry : inputTableMap.entrySet()) {
			    String inputTableName = entry.getKey();
			    LinkedHashMap<String,String> inputTableFields = entry.getValue();
			    LinkedHashMap<String,String> columnCatMap = new LinkedHashMap<>();
			    LinkedHashMap<String,String> mappedMap = new LinkedHashMap<>();
			    for (String inputColumnName : inputTableFields.keySet()) {
			    	 String cat = "";
			    		if(!inputColumnName.startsWith("PK") && !inputColumnName.startsWith("FK")){
			    			Map<String,List<String>> matchedWords = new LinkedHashMap<>();
			    			
			    			matchedWords = findWordMatchingPossibilities.findMatchingWord(inputColumnName,domainType);
			    			if(matchedWords!= null && !matchedWords.isEmpty())
			    			{
			    				Set<String> entires = matchedWords.keySet();
						         for(String ent:entires){
						        	 if(cat == null || cat.isEmpty())
						        		 cat = ent;
						        	 else
						        		 cat = cat+" "+ ent;
						         }
			    			}
			    			columnCatMap.put(inputColumnName, cat);
					    	mappedMap.put(inputColumnName+"<>"+cat, inputTableFields.get(inputColumnName));	
			    		}
			    }
			    availableTables.put(inputTableName, columnCatMap);
			    finalMappedMap.put(inputTableName, mappedMap);
			    System.out.println("----columnCatMap is--"+columnCatMap.toString());
			}
		}
		fos.close();
		System.out.println("----availableTables is--"+availableTables.toString());
		System.out.println("----finalMappedMap is--"+finalMappedMap.toString());
		for (Map.Entry<String, LinkedHashMap<String,String>> entry : inputTableMap.entrySet()) {
			LinkedHashMap<String,String> inputTableFields = entry.getValue();
			LinkedHashMap<String,String> finalInputTableFields = new LinkedHashMap<>();
			for (String inputColumnName : inputTableFields.keySet()) {
				if(!inputColumnName.startsWith("PK") && !inputColumnName.startsWith("FK"))
				       finalInputTableFields.put(inputColumnName, inputTableFields.get(inputColumnName));
				
			}
			finalInputMap.put(entry.getKey(), finalInputTableFields);
		}
		List<LinkedHashMap<String, LinkedHashMap<String,String>>> list = new ArrayList<>();
		list.add(finalInputMap);
		list.add(finalMappedMap);
		return list;
		
		// Unmapped entity needs to be saved?
	}
}
