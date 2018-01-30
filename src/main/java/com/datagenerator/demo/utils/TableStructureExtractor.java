package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

@Component
public class TableStructureExtractor {

	public LinkedHashMap<String, LinkedHashMap<String,String>> searchforTableName(File file) throws FileNotFoundException {
		LinkedHashMap<String, LinkedHashMap<String,String>> tableMap = new LinkedHashMap<>();
		/*ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("Dump20180122.sql").getFile());*/
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
		//		String field = fieldString[1].split(" ")[0].replace("`", "");
		//		String fkRelationship = fieldString[0].split(" ")[3].replace("`", "");
				String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
				String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "").replace("ONUPDATECASCADE", "");
				fieldMap.put("FK"+fkCount+"->"+test1,test2);
		//		fkList.add(test1+"-"+test2);
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
			if(tableName != "" || null != tableName && fieldMap!=null)
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		reOrderTableStructure(tableMap);
		return tableMap;
	}
	
	private void reOrderTableStructure(LinkedHashMap<String, LinkedHashMap<String,String>> tableMap){
		LinkedHashMap<String, List<String>>  fkListMap = new LinkedHashMap<>();
		
		  List<String> noForeignList = new LinkedList<String>();
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
		    		
		    		/*LinkedHashMap<String,String> tableFields2 = tableMap.get(tblName);
		    		for (String inputColumnName2 : tableFields2.keySet()) {
		    			if(inputColumnName2.startsWith("FK")){
		    				String[] fieldString2 = inputColumnName2.split("->");
				    		String field2 = fieldString2[1];
				    		String association2 = tableFields2.get(inputColumnName2);
				    		System.out.println("tblName is ---"+field2);
				    		System.out.println("association is ---"+association2);
				    		String[] associationSplit2 = association2.split("\\(");
				    		String tblName2  = associationSplit2[0];
				    		String colName2  = associationSplit2[1].replace(")", "");
				    		System.out.println("tblName2 is ---"+tblName2+"---colName2--"+colName2);
		    				
		    			}
		    		}*/
		    		//list.add(inputColumnName+"-"+tableFields.get(inputColumnName));
		    		list.add(tblName);
		    	}
		    }
//		    if(list != null && !list.isEmpty())
		         fkListMap.put(tableName, list);
//		    else
//		    	noForeignList.add(tableName);
			
		}
		
//		fkListMap.put("NOFK", noForeignList);
		LinkedHashMap<String, List<String>> sorted = fkListMap.entrySet().stream()
		        .sorted(comparingInt(e->e.getValue().size()))
		        .collect(toMap(
		                Map.Entry::getKey,
		                Map.Entry::getValue,
		                (a,b) -> {throw new AssertionError();},
		                LinkedHashMap::new
		        ));
		
		System.out.println(sorted.toString());
		doPairing(sorted);
		JSONObject json1 = new JSONObject(sorted);
		System.out.println(json1); 
		
	}
	
	
	
	
	private void doPairing(LinkedHashMap<String, List<String>> sorted) {
		LinkedHashMap<String, List<String>> paired=new LinkedHashMap<>();
		ArrayList<String> lst=new ArrayList<>();
		Set<Entry<String,List<String>>> set=sorted.entrySet();
		for(Entry<String,List<String>> s:set) {
		   List<String> lst1=s.getValue();
		if(lst1.isEmpty()) {
			paired.put(s.getKey(),new ArrayList<String>());
		}else {
			for(String str:lst1) {
			   List<String> val=new ArrayList<>();
			   if(paired.get(str)!=null)
			       val=paired.get(str);
			   val.add(s.getKey());
			   paired.put(str, val);
		   }}
		   
		   
			
		}
		
		
		System.out.println("paired Map-----------------"+paired);
		
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
