package com.datagenerator.demo.utils;

import com.datagenerator.demo.serviceImpl.Dataset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GenerateSampleDataUtil {

  public static List<List<String>> generateData(Map<String, String> fieldMap, LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap, int rowCount, Map<String, List<String>> concurrentMap, String tableName) {
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(fieldMap.keySet()));
    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
        String[] values = entry.getValue().split("<>");
        List <String> fkValues=concurrentMap.get(tableName+"."+entry.getKey());
        if(fkValues!=null){
          row.add(fkValues.get(i));
        }
        else if (values[1].contains("varchar")) {
          row.add(Dataset.getRandomData(values[0]));
        } else if (values[1].contains("int")) {
          row.add(Dataset.getRandomInt());
        } else if (values[1].contains("blob")) {
          row.add(Dataset.getRandomData(values[0]));
        } else if (values[1].contains("date")) {
          row.add(Dataset.getRandomDate().toString());
        } else if (values[1].contains("text")) {
          row.add(Dataset.getRandomData(values[0]));
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
