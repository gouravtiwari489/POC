package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.domain.Domain;
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
	//	List<Domain>  domains = domainRepository.getDomainByDomainId(domainType);
	//	List<Object> tables = domains.get(0).getTables();
		Map<String, LinkedHashMap<String, String>> availableTables = new LinkedHashMap<>();
		/*for(Object table:tables){
			 Map<String, LinkedHashMap<String, String>> tbl = new LinkedHashMap<>();
			 tbl  = (Map<String, LinkedHashMap<String, String>>) table;
			 for (Map.Entry<String, LinkedHashMap<String,String>> entry : tbl.entrySet()) {
				 availableTables.put(entry.getKey(), entry.getValue());
			 }
		}*/
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
			    	//	 columnCatMap.put(inputColumnName, cat);
			    	//	 mappedMap.put(cat, inputTableFields.get(inputColumnName));
			    		
			    }
			    availableTables.put(inputTableName, columnCatMap);
			    finalMappedMap.put(inputTableName, mappedMap);
			    System.out.println("----columnCatMap is--"+columnCatMap.toString());
				/*for (Map.Entry<String, LinkedHashMap<String,String>> availablemap : availableTables.entrySet()) {
					boolean tableMatched = false;
					boolean fieldsMatched = false;
					String inPutkeysList = "";
					String mappedkeysList = "";
					String tablekey = availablemap.getKey();
					LinkedHashMap<String,String> fieldsvalue = availablemap.getValue();
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
				    
		
					if (counter > 0 && inputTableFieldsSize > 0) {
						int percentage = (counter / inputTableFieldsSize) * 100;
						if (percentage >= 80)
							fieldsMatched = true;
					}
				    for (String inputColumnName : inputTableFields.keySet()) {
				    	if(inPutkeysList == null || inPutkeysList.isEmpty())
				    		inPutkeysList = inputColumnName;
				    	else
				    		inPutkeysList = inPutkeysList + "," + inputColumnName;
				    }
				    if(!inputTableFields.containsKey("KEYS"))
				    	inputTableFields.put("KEYS", inPutkeysList);
				    finalInputMap.put(inputTableName, inputTableFields);
				    if(tableMatched && fieldsMatched){
				    	LinkedHashMap<String,String> fieldsvalue2 = new LinkedHashMap<>();
				    	 Set<Entry<String, String>> entires = fieldsvalue.entrySet();
				         for(Entry<String,String> ent:entires){
				             fieldsvalue2.put(ent.getKey()+" "+ent.getKey()+" "+ent.getKey(), ent.getValue());
				         }
				    	finalMappedMap.put(tablekey, fieldsvalue2);
				    	for(String availableColumn : fieldsvalue.keySet()){
				    		if(mappedkeysList == null || mappedkeysList.isEmpty())
				    			mappedkeysList = availableColumn;
					    	else
					    		mappedkeysList = mappedkeysList + "," + availableColumn;
				    	}
				    	if(!fieldsvalue.containsKey("KEYS"))
				         	 fieldsvalue.put("KEYS", mappedkeysList);
				    	
				    }
				}*/
			}
		}
		fos.close();
		System.out.println("----availableTables is--"+availableTables.toString());
		System.out.println("----finalMappedMap is--"+finalMappedMap.toString());
		/*JSONObject json1 = new JSONObject(finalInputMap);
		System.out.println(json1); 
		System.out.println("###############");
		
		JSONObject json2 = new JSONObject(finalMappedMap);
		
		
		System.out.println(json2); */
		
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
		//list.add(inputTableMap);
		list.add(finalInputMap);
		list.add(finalMappedMap);
		return list;
		
		// Unmapped entity needs to be saved?
	}
}
