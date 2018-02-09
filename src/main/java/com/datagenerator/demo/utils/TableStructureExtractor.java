package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.serviceImpl.MapToListTransformerService;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

@Component
public class TableStructureExtractor {
	
	@Autowired
	private MapToListTransformerService  mapToListTransformerService;

	public LinkedHashMap<String, LinkedHashMap<String,String>> searchforTableName(File file) throws FileNotFoundException {
		LinkedHashMap<String, LinkedHashMap<String,String>> tableMap = new LinkedHashMap<>();
		final Scanner scanner = new Scanner(file);
		String tableName = "",primaryKey = "";
		LinkedHashMap<String, String> fieldMap = null;
		int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains("CREATE TABLE ")) {
				fieldMap = new LinkedHashMap<>();
				tableName = "";
				fkCount = 1;
				String[] matchString = lineFromFile.split("CREATE TABLE ");
				tableName = matchString[1].split(" ")[0].replace("`", "");
				count = 1;				
			} else if (lineFromFile.contains("PRIMARY KEY ")) {
				primaryKey = "";
				count =0;
				String[] pkString = lineFromFile.split("PRIMARY KEY ");
				primaryKey = pkString[1].split(" ")[0].replace("`", "");
				if (primaryKey.length() > 1)
					fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 2));
			} 
			else if (lineFromFile.contains("FOREIGN KEY ")) {
				String[] fieldString = lineFromFile.split("FOREIGN KEY ");
				String[] fieldString2 = fieldString[1].split("REFERENCES ");
				System.out.println("fieldString for fk is::"+fieldString2[0]+"--"+fieldString2[1]);
				String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
				String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "").replace("ONUPDATECASCADE", "");
				fieldMap.put("FK"+fkCount+"->"+test1,test2);
				count =0;
				fkCount++;
			}else if(lineFromFile.contains("ENGINE")) {
				count =0;
			} 
			else if(count==1){
				String[] fieldString = lineFromFile.split(" ");
				if(fieldString.length>2) {
					String field = fieldString[2].replace("`", "");
					String fieldType = fieldString[3].replace("`", "");
					fieldType = fieldType.replace(",", "");
					fieldMap.put(field,fieldType);
				}
			}
			if((tableName != "" && null != tableName) && (fieldMap!=null && !fieldMap.isEmpty()))
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		reOrderTableStructure(tableMap);
		return tableMap;
	}
	
	private void reOrderTableStructure(LinkedHashMap<String, LinkedHashMap<String,String>> tableMap){
		LinkedHashMap<String, List<String>>  fkListMap = new LinkedHashMap<>();
		for (Map.Entry<String, LinkedHashMap<String,String>> entry : tableMap.entrySet()) {
			String tableName = entry.getKey();
		    LinkedHashMap<String,String> tableFields = entry.getValue();
		    List<String> list = new LinkedList<String>();
		    for (String inputColumnName : tableFields.keySet()) {
		    	System.out.println("inputColumnName is ---"+inputColumnName);
		    	if(inputColumnName.startsWith("FK")){
		    		String[] fieldString = inputColumnName.split("->");
		    		String field = fieldString[1];
		    		String association = tableFields.get(inputColumnName);
		    		System.out.println("tblName is ---"+field);
		    		System.out.println("association is ---"+association);
		    		String[] associationSplit = association.split("\\(");
		    		String tblName  = associationSplit[0];
		    		String colName  = associationSplit[1].replace(")", "");
		    		System.out.println("tblName is ---"+tblName+"---colName--"+colName);
		    		list.add(tblName);
		    	}
		    }
		         fkListMap.put(tableName, list);
		}
		LinkedHashMap<String, List<String>> sorted = sortMap(fkListMap);
		System.out.println(sorted.toString());
		doPairing(sorted);
		JSONObject json1 = new JSONObject(sorted);
		System.out.println(json1); 
		
	}
	
	private LinkedHashMap<String, List<String>> doPairing(LinkedHashMap<String, List<String>> sorted) {
		LinkedHashMap<String, List<String>> paired=new LinkedHashMap<>();
			
		sorted.forEach((k,v)->{
			
			paired.put(k,new ArrayList<String>());
			
			if(v.isEmpty()) {
				paired.put(k,new ArrayList<String>());
			}else {				
				v.stream().forEach(index->{
					 List<String> val=new ArrayList<>();
					 if(paired.get(index)!=null)
					       val=paired.get(index);
					   val.add(k);
					   paired.put(index, val);
				});
			}
			
		});
		
		return sortMap(paired);
	}
	
	private LinkedHashMap<String, List<String>> sortMap(LinkedHashMap<String, List<String>> inputMap){
		return inputMap.entrySet().stream()
        .sorted(comparingInt(e->e.getValue().size()))
        .collect(toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a,b) -> {throw new AssertionError();},
                LinkedHashMap::new
        ));
		
		
	}
	public List<GenerateDataObject> getFKMap() throws FileNotFoundException {
		LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = new LinkedHashMap<>();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("mysqlsampledatabase.sql").getFile());
		final Scanner scanner = new Scanner(file);
		String tableName = "", primaryKey = "";
		LinkedHashMap<String, String> fieldMap = null;
		int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains("CREATE TABLE ")) {
				fieldMap = new LinkedHashMap<>();
				tableName = "";
				fkCount = 1;
				String[] matchString = lineFromFile.split("CREATE TABLE ");
				tableName = matchString[1].split(" ")[0].replace("`", "");
				count = 1;
			} else if (lineFromFile.contains("PRIMARY KEY ")) {
				primaryKey = "";
				count = 0;
				String[] pkString = lineFromFile.split("PRIMARY KEY ");
				primaryKey = pkString[1].split(" ")[0].replace("`", "");
				if (primaryKey.length() > 1)
					fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 2));
			} else if (lineFromFile.contains("FOREIGN KEY ")) {
				String[] fieldString = lineFromFile.split("FOREIGN KEY ");
				String[] fieldString2 = fieldString[1].split("REFERENCES ");
				System.out.println("fieldString for fk is::" + fieldString2[0] + "--" + fieldString2[1]);
				String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
				String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "")
						.replace("ONUPDATECASCADE", "");
				fieldMap.put("FK" + fkCount + "->" + test1, test2);
				count = 0;
				fkCount++;
			} else if (lineFromFile.contains("ENGINE")) {
				count = 0;
			} else if (count == 1) {
				String[] fieldString = lineFromFile.split(" ");
				if (fieldString.length > 2) {
					String field = fieldString[2].replace("`", "");
					String fieldType = fieldString[3].replace("`", "");
					fieldMap.put(field, fieldType);
				}
			}
			if (tableName != "" || null != tableName && fieldMap != null)
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		LinkedHashMap<String, List<String>> fkListMap = new LinkedHashMap<>();
		for (Map.Entry<String, LinkedHashMap<String, String>> entry : tableMap.entrySet()) {
			String tableName2 = entry.getKey();
			LinkedHashMap<String, String> tableFields = entry.getValue();
			List<String> list = new LinkedList<String>();
			for (String inputColumnName : tableFields.keySet()) {
				System.out.println("inputColumnName is ---" + inputColumnName);
				if (inputColumnName.startsWith("FK")) {
					String[] fieldString = inputColumnName.split("->");
					String field = fieldString[1];
					String association = tableFields.get(inputColumnName);
					System.out.println("tblName is ---" + field);
					System.out.println("association is ---" + association);
					String[] associationSplit = association.split("\\(");
					String tblName = associationSplit[0];
					String colName = associationSplit[1].replace(")", "");
					System.out.println("tblName is ---" + tblName + "---colName--" + colName);

					list.add(tblName);
				}
			}
			fkListMap.put(tableName2, list);

		}
		LinkedHashMap<String, List<String>> sorted = fkListMap.entrySet().stream()
				.sorted(comparingInt(e -> e.getValue().size()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		System.out.println(sorted.toString());
		mapToListTransformerService.setTableMap(sorted);
		return mapToListTransformerService.transform();
	}

}
