package com.datagenerator.demo.utils;

import com.datagenerator.demo.serviceImpl.Dataset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GenerateSampleDataUtil {

  static List<String> primaryKeyList = new ArrayList<>();

  public static List<List<String>> generateData(
      Map<String, String> fieldMap,
      LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap,
      int rowCount,
      Map<String, List<String>> concurrentMap,
      String tableName) {
    String primaryKey = linkedHashMap.get(tableName).get("PK");
    if(primaryKey.contains(",")) {
    	String [] compositeKey = primaryKey.split(",");
    	String compositeTable = tableName;
    }
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(fieldMap.keySet()));
    int counter = 10000;
    int incrementString = 2000;
    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
        String[] values = entry.getValue().split("<>");
        String maxLength = null;
        String []dataTypes = values[1].split("[\\(\\)]");
        if(dataTypes.length > 1 &&dataTypes[1]!=null) {
        	maxLength = dataTypes[1];
        }
        List<String> fkValues = concurrentMap.get(tableName + "." + entry.getKey());
        if (fkValues != null) {
          row.add(fkValues.get(i));
        } else if (values[1].contains("varchar")) {
          if (entry.getKey().equals(primaryKey)) {
        	  incrementString++;
            row.add(entry.getKey().substring(0, 3)+ incrementString);
          } else {
            row.add(Dataset.getRandomData(values[0], maxLength));
          }
        } else if (values[1].contains("int")) {
          if (entry.getKey().equals(primaryKey)) {
            counter++;
            row.add(counter + "");
          } else {
            row.add(Dataset.getRandomInt(maxLength));
          }
        } else if (values[1].contains("blob")) {
          row.add(Dataset.getRandomData(values[0], maxLength));
        } else if (values[1].contains("date")) {
          row.add(Dataset.getRandomDate().toString());
        } else if (values[1].contains("text")) {
          row.add(Dataset.getRandomData(values[0], maxLength));
        } else {
          try {
            row.add(Dataset.getRandomDecimal());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      records.add(row);
    }

    return records;
  }
}
