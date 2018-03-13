package com.osi.dgen.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class RelationalDataExtractor {

  public static Map<String, List<String>> extractdata(
      LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap,
      List<List<String>> excelData,
      String tableName) {

    Map<String, List<String>> dataMap = new HashMap<>();
    for (Entry<String, LinkedHashMap<String, String>> entry : linkedHashMap.entrySet()) {
      for (Entry<String, String> map : entry.getValue().entrySet()) {

        if (map.getValue().contains(tableName) && map.getKey().contains("FK")) {
          String[] str = map.getValue().split("[\\(\\)]");
          dataMap.put(
              entry.getKey() + "." + map.getKey().split("->")[1],
              copyData(excelData, linkedHashMap.get(tableName), str[1]));
        }
      }
    }
    return dataMap;
  }

  private static List<String> copyData(
      List<List<String>> excelData, Map<String, String> map, String FieldName) {
    List<String> data = null;
    int counter = 0;
    for (Entry<String, String> entry : map.entrySet()) {
      if (entry.getKey().equals(FieldName)) {
        data = getColumndata(excelData, counter);
      } else {

        counter++;
      }
    }
    return data;
  }

  private static List<String> getColumndata(List<List<String>> excelData, int index) {
    List<String> data = new ArrayList<>();
    for (List<String> list : excelData) {
      data.add(list.get(index));
    }
    return data;
  }
}
