package com.osi.datagen.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.osi.datagen.datagen.DataGenFactory;
import com.osi.datagen.datagen.intf.IDataGenerator;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.Table;

@Service
public class GenerateSampleDataUtil {

  public static List<List<String>> generateData(
      Map<String, String> fieldMap,
      LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap,
      int rowCount,
      Map<String, List<String>> concurrentMap,
      String tableName,
      String domainType)
      throws ParseException {

    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(fieldMap.keySet()));

    DataGenFactory.map.clear();
    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
        Field field = getField(linkedHashMap, tableName, entry);
        List<String> fkValues = concurrentMap.get(tableName + "." + entry.getKey());
        if (fkValues != null) {
          row.add(fkValues.get(i));
        } else {
          IDataGenerator generator =
              DataGenFactory.createDataGenerator(entry.getValue().split("<>")[1], domainType);
          row.add(generator.generateData(field));
        }
      }
      records.add(row);
    }

    return records;
  }

  
  public static List<List<String>> generateData(Table table, int rowCount,
      Map<String, List<String>> concurrentMap, String domainType) throws ParseException {
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(table.getFieldsNames()));

    //DataGenFactory.map.clear();
    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Field field : table.getFields())  
     {
        List<String> fkValues = concurrentMap.get(table.getTableName() + "." + field.getColumnName());
        if (fkValues != null) {
          row.add(fkValues.get(i));
        } else {
          IDataGenerator generator =
              DataGenFactory.createDataGenerator(field.getDataType(), domainType);
          row.add(generator.generateData(field));
        }
      }
      records.add(row);
    }

    return records;
  }
}
