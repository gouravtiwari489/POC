package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class XmiTableStructureExtractor {

  public LinkedHashMap<String, LinkedHashMap<String, String>> searchforTableName()
      throws FileNotFoundException {
    LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = new LinkedHashMap<>();
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("Emp.xmi").getFile());
    final Scanner scanner = new Scanner(file);
    String tableName = "";
    String primaryKey = "";
    LinkedHashMap<String, String> fieldMap = null;
    int count = 0, fkCount = 0;
    while (scanner.hasNextLine()) {
      final String lineFromFile = scanner.nextLine();
      // Table Name Search
      if (lineFromFile.contains("UML:Class")
          && !lineFromFile.contains("/UML:Class")
          && !lineFromFile.contains("UML:Classifier")
          && !lineFromFile.contains("/UML:Classifier")) {
        fieldMap = new LinkedHashMap<>();
        tableName = "";
        fkCount = 0;
        String[] matchString = lineFromFile.split("name");
        tableName = matchString[1].substring(2, matchString[1].indexOf(" ") - 1);
        count = 1;
      } else if (lineFromFile.contains("PRIMARY KEY ")) {
        // Primary Key Search
        primaryKey = "";
        count = 0;
        String[] pkString = lineFromFile.split("PRIMARY KEY ");
        primaryKey = pkString[1].split(" ")[0].replace("`", "");
        if (primaryKey.length() > 1)
          fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 2));
      } else if (lineFromFile.contains("FOREIGN KEY ")) {
        fkCount++;
        String[] fieldString = lineFromFile.split("FOREIGN KEY ");
        String field = fieldString[1].split(" ")[0].replace("`", "");
        String fkRelationship = fieldString[0].split(" ")[3].replace("`", "");
        fieldMap.put("FK" + fkCount, field.substring(1, field.length() - 1));
        count = 0;
      } else if (lineFromFile.contains("ENGINE")) {
        count = 0;
      } else if (lineFromFile.contains("UML:Attribute")) {
        String[] fieldString = lineFromFile.split("name");
        String fieldName = fieldString[1].substring(2, fieldString[1].indexOf(" ") - 1);
        fieldMap.put(fieldName, "varchar(100)");
      }
      if ((!tableName.equals("") && null != tableName) && (fieldMap != null && fieldMap.size() > 0))
        tableMap.put(tableName, fieldMap);
    }
    scanner.close();

    System.out.println("List of Table Names : ");
    Set set = tableMap.entrySet();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      Entry<String, Map> entry = (Entry<String, Map>) itr.next();
      System.out.println("###################### " + entry.getKey() + " ######################");
      Map fieldsMap = entry.getValue();

      if (fieldsMap != null) {
        Set fieldSet = fieldsMap.entrySet();
        Iterator it = fieldSet.iterator();
        while (it.hasNext()) {
          Entry<String, String> fieldEntry = (Entry<String, String>) it.next();
          System.out.println(fieldEntry.getKey() + " :: " + fieldEntry.getValue());
        }
      }
      System.out.println("############################################################# ");
    }

    return tableMap;
  }
}
