package com.datagenerator.demo.utils;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TableStructureExtractor {

  @Value("${dependencycheck.toggle}")
  String toggleCheck;

  private static final String ENGINE = "ENGINE";
  private static final String REFERENCES = "REFERENCES ";
  private static final String FOREIGN_KEY = "FOREIGN KEY ";
  private static final String PRIMARY_KEY = "PRIMARY KEY ";
  private static final String CREATE_TABLE = "CREATE TABLE ";
  private static final String PARSE_iSSUE = "There is an issue wth sql file.";

  @Autowired private CustomTokenConverter customTokenConverter;

  public LinkedHashMap<String, LinkedHashMap<String, String>> searchforTableName(
      File file, boolean dependencyCheck) throws Exception {
    LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = new LinkedHashMap<>();
    try {
		final Scanner scanner = new Scanner(file);
		String tableName = "", primaryKey = "";
		LinkedHashMap<String, String> fieldMap = null;
		int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
		  final String lineFromFile = scanner.nextLine();
		  if (lineFromFile.contains(CREATE_TABLE)) {
		    fieldMap = new LinkedHashMap<>();
		    tableName = "";
		    fkCount = 1;
		    String[] matchString = lineFromFile.split(CREATE_TABLE);
		    tableName = matchString[1].split(" ")[0].replace("`", "");
		    count = 1;
		  } else if (lineFromFile.contains(PRIMARY_KEY)) {
		    primaryKey = "";
		    count = 0;
		    String[] pkString = lineFromFile.split(PRIMARY_KEY);
		    primaryKey = pkString[1].split(" ")[0].replace("`", "");
		    String[] pkString2 = null;
		    if(primaryKey.endsWith(",")) {
		    	 pkString2 = primaryKey.split(",");
		    	  if(pkString2.length >1)
				         primaryKey = pkString2[0]+"," + pkString2[1];
				    else
				    	primaryKey = pkString2[0];
		    }
		    if (primaryKey.length() > 1)
		      fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 1));
		  } else if (lineFromFile.contains(FOREIGN_KEY)) {
		    String[] fieldString = lineFromFile.split(FOREIGN_KEY);
		    String[] fieldString2 = fieldString[1].split(REFERENCES);
		    System.out.println("fieldString for fk is::" + fieldString2[0] + "--" + fieldString2[1]);
		    String test1 =
		        fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
		    String test2 =
		        fieldString2[1]
		            .replace(" ", "")
		            .replace("`", "")
		            .replace(",", "")
		            .replace("ONUPDATECASCADE", "");
		    fieldMap.put("FK" + fkCount + "->" + test1, test2);
		    count = 0;
		    fkCount++;
		  } else if (lineFromFile.contains(ENGINE)) {
		    count = 0;
		  } else if (count == 1) {
		    String[] fieldString = lineFromFile.split(" ");
		    if (fieldString.length > 2) {
		      String field = fieldString[2].replace("`", "");
		      String fieldType = fieldString[3].replace("`", "");
		      fieldType = fieldType.replace(",", "");
		      fieldMap.put(field, fieldType);
		    }
		  }
		  if ((tableName != "" && null != tableName) && (fieldMap != null && !fieldMap.isEmpty()))
		    tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		reOrderTableStructure(tableMap, dependencyCheck);
	} catch (Exception e) {
		e.printStackTrace();
		throw new Exception(e.getMessage());
	}
    return tableMap;
  }

  private void reOrderTableStructure(
      LinkedHashMap<String, LinkedHashMap<String, String>> tableMap, boolean dependencyCheck)
      throws Exception {
    LinkedHashMap<String, List<String>> fkListMap = createFKListMap(tableMap);
    String msg = "";
    String message = "";
    boolean dependCheck = Boolean.parseBoolean(toggleCheck);
    for (Map.Entry<String, List<String>> entry : fkListMap.entrySet()) {
      String parentTbl = entry.getKey();
      List<String> childTbls = entry.getValue();
      if (childTbls.contains(parentTbl)) msg = "Table " + parentTbl + " has self join";
      else {
        for (String chldtbl : childTbls) {
          List<String> childTbls2 = fkListMap.get(chldtbl);
          if (childTbls2.contains(parentTbl)) {
            if (!msg.isEmpty())
              msg = msg + " and   " + parentTbl + " has cyclic dependency with " + chldtbl;
            else msg = parentTbl + " has cyclic dependency with " + chldtbl;
          }
        }
      }
    }

    if (!msg.isEmpty()) {
      message = dependCheck ? message = "Error!" + msg : "Warning!" + msg;
      if (dependCheck) {
        throw new Exception(message);
      } else if (!dependCheck && dependencyCheck) {
        throw new Exception(message);
      } else if (!dependCheck && !dependencyCheck) {
        System.out.println("dependency ignored and proceed");
      }
    }

    LinkedHashMap<String, List<String>> sorted = sortMap(fkListMap);
    Map<Integer, List<String>> map = transform(sorted);
    customTokenConverter.setAdditionalInfo("orderedFKList", map);
    System.out.println(customTokenConverter.getAdditionalInfo("orderedFKList"));
  }

  private LinkedHashMap<String, List<String>> sortMap(
      LinkedHashMap<String, List<String>> inputMap) {
    return inputMap
        .entrySet()
        .stream()
        .sorted(comparingInt(e -> e.getValue().size()))
        .collect(
            toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> {
                  throw new AssertionError();
                },
                LinkedHashMap::new));
  }

  private LinkedHashMap<String, List<String>> createFKListMap(
      LinkedHashMap<String, LinkedHashMap<String, String>> tableMap) {
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
    return fkListMap;
  }

  public Map<Integer, List<String>> transform(LinkedHashMap<String, List<String>> tableMap) {
    Map<Integer, List<String>> newMap = new LinkedHashMap<Integer, List<String>>();
    for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
      transformMaptoListData_1(newMap, entry.getKey(), entry.getValue(), tableMap);
    }
    return newMap;
  }

  private void transformMaptoListData_1(
      Map<Integer, List<String>> orderedMap,
      String child,
      List<String> list,
      LinkedHashMap<String, List<String>> tableMap) {
    for (String parentTable : list) {
      Integer parentOrderLevel = 0;
      Integer childOrderLevel = 0;
      for (Map.Entry<Integer, List<String>> entry : orderedMap.entrySet()) {
        if (entry.getValue().contains(parentTable) && parentOrderLevel < entry.getKey()) {
          parentOrderLevel = entry.getKey();
        }
        if (entry.getValue().contains(child)) {
          childOrderLevel = entry.getKey();
        }
      }
      if (parentOrderLevel == 0) {
        parentOrderLevel = addChildIntoMap(parentTable, orderedMap, parentOrderLevel);
      }
      if (childOrderLevel > 0) {
        if (parentOrderLevel >= childOrderLevel) // if parent order
        // number is greater
        // than child then put
        // child below parent or
        // leave as it is in
        // order
        {
          if (!isTablesOnCyclicDependencies(
              parentOrderLevel, childOrderLevel, parentTable, child, tableMap)) {
            orderedMap.get(childOrderLevel).remove(child);
            parentOrderLevel = addChildIntoMap(child, orderedMap, parentOrderLevel);
            findChildDependencies(child, orderedMap, parentOrderLevel, tableMap);
          }
        }
      } else {
        addChildIntoMap(child, orderedMap, parentOrderLevel);
      }
    }
    if (list.size() == 0) {
      addChildIntoMap(child, orderedMap, 0);
    }
  }

  private boolean isTablesOnCyclicDependencies(
      Integer parentOrderLevel,
      Integer childOrderLevel,
      String parentTable,
      String child,
      LinkedHashMap<String, List<String>> tableMap) {
    boolean isTrue = false;
    if (parentOrderLevel.intValue() == childOrderLevel.intValue()) {
      for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
        if (entry.getKey().equals(child)) {
          break;
        }
        if (entry.getValue().contains(child) && entry.getKey().equals(parentTable)) {
          isTrue = true;
        }
      }
    }
    return isTrue;
  }

  private int addChildIntoMap(
      String child, Map<Integer, List<String>> newMap, Integer parentOrderLevel) {

    int newParentLevel = parentOrderLevel.intValue() + 1;
    List<String> OrderLevelist = newMap.get(newParentLevel);
    if (!(OrderLevelist != null && OrderLevelist.size() > 0)) {
      OrderLevelist = new ArrayList<String>();
      newMap.put(parentOrderLevel + 1, OrderLevelist);
    }
    newMap.get(newParentLevel).add(child);
    return newParentLevel;
  }

  private void findChildDependencies(
      String child,
      Map<Integer, List<String>> orderedMap,
      Integer parentOrderLevel,
      LinkedHashMap<String, List<String>> tableMap) {

    List<String> childDependentTables = findDependencies(child, child, tableMap);

    for (String childTable : childDependentTables) {
      ListIterator<Integer> litt = new LinkedList<Integer>(orderedMap.keySet()).listIterator();
      while (litt.hasNext()) {
        Integer entry = litt.next();
        if (orderedMap.get(entry).contains(childTable)) {
          if (entry <= parentOrderLevel) {
            List<String> parentDependentTables = findDependencies(childTable, child, tableMap);
            List<String> tables = orderedMap.get(entry);
            tables.remove(childTable);
            orderedMap.put(entry, tables);
            if (parentDependentTables.contains(child)) {
              addChildIntoMap(childTable, orderedMap, parentOrderLevel - 1);
              childTable = childTable + "-->>" + child;
              addChildIntoMap(childTable, orderedMap, parentOrderLevel - 1);
              continue;
            }
            addChildIntoMap(childTable, orderedMap, parentOrderLevel);
          }
        }
      }
    }
  }

  private List<String> findDependencies(
      String parent, String child, LinkedHashMap<String, List<String>> tableMap) {
    List<String> dependentTables = new ArrayList<String>();
    for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
      if (entry.getValue().contains(parent)) {
        dependentTables.add(entry.getKey());
      }
      if (entry.getKey().equals(child)) {
        break;
      }
    }
    return dependentTables;
  }
}
