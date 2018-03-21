package com.osi.datagen.datageneration.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.osi.datagen.datagenerators.DataGenFactory;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.Table;

@Service
public class GenerateSampleDataUtil {

  public static List<List<String>> generateData(Table table, int rowCount,
      Map<String, List<String>> concurrentMap, String domainType) throws ParseException {
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(table.getFieldsNames()));

    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Field field : table.getFields()) {
        List<String> fkValues =
            concurrentMap.get(table.getTableName() + "." + field.getColumnName());
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



  public static void  generateDataForColumn(List<Field> primaryKeyFields, int rowCount,
      String domainType, Map<String, List<String>> concurrentMap) throws ParseException {
    for (Field field : primaryKeyFields) {
      generateDataForField(field, rowCount, domainType);
    }
  }


  private static List<String>  generateDataForField(Field field,int rowCount,String domainType ) throws ParseException {
    
    List<String> row=new ArrayList<>();   
      for (int i = 1; i <= rowCount; i++) {
          IDataGenerator generator =
              DataGenFactory.createDataGenerator(field.getDataType(), domainType);
          row.add(generator.generateData(field));
  }

return row;

  }

  public static void generateUniquData(List<List<Field>> uniqueKeyFields,
      int rowCount, String domainType, Map<String, List<String>> concurrentMap) throws ParseException {
    for (List<Field> list : uniqueKeyFields) {
      for (Field field : list) {
        generateDataForField(field, rowCount, domainType);
        
      }
      
    }
  }
}
