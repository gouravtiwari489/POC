package com.osi.datagen.datageneration.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.osi.datagen.datagenerators.DataGenFactory;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.Tuple;

@Service
public class GenerateSampleDataUtil {

  public static List<List<String>> generateData(Table table, int rowCount,
      Map<Tuple, List<String>> concurrentMap, String domainType) throws ParseException {
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(table.getFieldsNames()));

    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Field field : table.getFields()) {
        List<String> fkValues =
            concurrentMap.get(new Tuple(table.getTableName() ,field.getColumnName()));
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



  public static void  generatePrimaryKeyData(List<Field> primaryKeyFields,String tableName, int rowCount,
      String domainType, List<Table> childTables, Map<Tuple, List<String>> concurrentMap) throws ParseException {
    for (Field field : primaryKeyFields) {
      List<String> data=generateDataForField(field, rowCount, domainType);      
      concurrentMap.put(new Tuple(tableName, field.getColumnName()),data);
      for (Table table : childTables) {
        List<String> strs= table.getForigenKeys().stream()
         .filter(fk->fk.getReferenceTable().equals(tableName)&&fk.getReferenceColumn().equals(field.getColumnName()))
         .map(k->k.getKeyName())
         .collect(Collectors.toList());
     strs.forEach(str->{
       concurrentMap.put(new Tuple(tableName,str),data);
     });
    }
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

  public static void generateUniqueData(List<List<Field>> uniqueKeyFields,
      String tableName,int rowCount, String domainType, List<Table> childTables, Map<Tuple, List<String>> concurrentMap) throws ParseException {
    for (List<Field> list : uniqueKeyFields) {
      for (Field field : list) {
        List<String> data=generateDataForField(field, rowCount, domainType);
        concurrentMap.put(new Tuple(tableName,field.getColumnName()),data);
        for (Table table : childTables) {
         List<String> strs= table.getForigenKeys().stream()
          .filter(fk->fk.getReferenceTable().equals(tableName)&&fk.getReferenceColumn().equals(field.getColumnName()))
          .map(k->k.getKeyName())
          .collect(Collectors.toList());
      strs.forEach(str->{
        concurrentMap.put(new Tuple(tableName,str),data);
      });
      
        }
          
      }
    }
  }
}
