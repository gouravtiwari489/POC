package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class TableStructureExtractor {

	public Map<String, Map<String,String>> searchforTableName() throws FileNotFoundException {
		Map<String, Map<String,String>> tableMap = new HashMap<>();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("Dump20180122.sql").getFile());
		final Scanner scanner = new Scanner(file);
		String tableName = "",primaryKey = "";
		Map<String, String> fieldMap = null;
		int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains("CREATE TABLE ")) {
				fieldMap = new HashMap<>();
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
				String field = fieldString[1].split(" ")[0].replace("`", "");
				String fkRelationship = fieldString[0].split(" ")[3].replace("`", "");
				fieldMap.put("FK"+fkCount+"->"+tableName,field.substring(1, field.length() - 1));
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
					fieldMap.put(field,fieldType);
				}
			}
			if(tableName != "" || null != tableName)
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		return tableMap;
	}
	
	/*private void printMetaData(Map<String, Map<String,String>> tableMap) {
		System.out.println("List of Table Names : ");
		Set set = tableMap.entrySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Entry<String, Map> entry = (Entry<String, Map>) itr.next();
			System.out.println("###################### Table Name : " + entry.getKey()); 
			Map fieldsMap = entry.getValue();
			
			if(fieldsMap != null) {
				Set fieldSet = fieldsMap.entrySet();
				Iterator it = fieldSet.iterator();
				while(it.hasNext()) {
					Entry<String, String> fieldEntry = (Entry<String, String>) it.next();
					System.out.println(fieldEntry.getKey()+" :: "+fieldEntry.getValue());
				}
			}
			System.out.println("###################### Table ####################### "); 
		}
	}*/
}
