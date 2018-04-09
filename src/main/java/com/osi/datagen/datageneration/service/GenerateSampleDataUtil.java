package com.osi.datagen.datageneration.service;

import static com.osi.datagen.datageneration.service.DataGenUtil.removeSingleQuotes;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.datagenerators.DataGenFactory;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.Tuple;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GenerateSampleDataUtil {

  public static List<List<String>> generateData(
      Table table,
      int rowCount,
      Map<Tuple, List<String>> concurrentMap,
      String domainType,
      String preferredLocale,
      DataGenFactory dataGenFactory)
      throws ParseException {
    List<List<String>> records = new ArrayList<List<String>>();
    records.add(new ArrayList<String>(table.getFieldsNames()));
    for (int i = 1; i <= rowCount; i++) {
      List<String> row = new ArrayList<String>();
      for (Field field : table.getFields()) {
        List<String> fkValues =
            concurrentMap.get(new Tuple(table.getTableName(), field.getColumnName()));
        if (fkValues != null) {
          row.add(fkValues.get(i - 1));
        } else {
          IDataGenerator generator = dataGenFactory.findDataGenerator(field.getDataType());
          if (field.getColumnName().contains("email")) {
            getEmail(records, row);
          } else {
            row.add(generator.generateData(field));
          }
        }
      }

      records.add(row);
    }
    for (Field field : table.getFields()) {
      concurrentMap.remove(new Tuple(table.getTableName(), field.getColumnName()));
    }
    return records;
  }

  private static void getEmail(List<List<String>> records, List<String> row) {
    int count = 0;
    List<String> nameList =
        records.get(0).stream().filter(st -> st.contains("Name")).collect(Collectors.toList());
    if (nameList.contains("lastName") && nameList.contains("firstName")) {
      int lastNameIndex = records.get(0).indexOf("lastName");
      int firstNameIndex = records.get(0).indexOf("firstName");
      String lastName = removeSingleQuotes(row.get(lastNameIndex));
      String firstName = removeSingleQuotes(row.get(firstNameIndex));
      row.add(firstName.concat(lastName).concat(DasConstants.EMAIL_PREFIX));
    } else if (nameList.contains("Name")) {
      int nameIndex = records.get(0).indexOf("name");
      String name = removeSingleQuotes(row.get(nameIndex));
      row.add(name.concat(DasConstants.EMAIL_PREFIX));
    } else {
      row.add("info" + count++ + DasConstants.EMAIL_PREFIX);
    }
  }

  public static void generatePrimaryKeyData(
      List<Field> primaryKeyFields,
      String tableName,
      int rowCount,
      String domainType,
      List<Table> childTables,
      Map<Tuple, List<String>> concurrentMap,
      DataGenFactory dataGenFactory)
      throws ParseException {
    for (Field field : primaryKeyFields) {
      List<String> data = generateDataForField(field, rowCount, dataGenFactory);
      concurrentMap.put(new Tuple(tableName, field.getColumnName()), data);
      for (Table table : childTables) {
        List<String> columns =
            table
                .getForigenKeys()
                .stream()
                .filter(
                    fk ->
                        fk.getReferenceTable().equals(tableName)
                            && fk.getReferenceColumn().equals(field.getColumnName()))
                .map(k -> k.getKeyName())
                .collect(Collectors.toList());
        columns.forEach(
            column -> {
              boolean isUnique =
                  table.getUniqueKeys().stream().anyMatch(c -> (c.getColumns().contains(column)));
              if (isUnique) {
                concurrentMap.put(new Tuple(table.getTableName(), column), data);
              } else {
                List<String> depValues = new ArrayList<>();
                for (int i = 0; i <= table.getRowCount(); i++) {
                  depValues.add(data.get(new Random().nextInt(data.size())));
                }
                concurrentMap.put(new Tuple(table.getTableName(), column), depValues);
              }
            });
      }
    }
  }

  private static List<String> generateDataForField(
      Field field, int rowCount, DataGenFactory dataGenFactory) throws ParseException {

    List<String> row = new ArrayList<>(rowCount);
    for (int i = 1; i <= rowCount; i++) {
      IUniqueDataGenerator generator = dataGenFactory.findUniqueDataGenerator(field.getDataType());
      row.add(generator.generateUniqueData(field));
    }

    return row;
  }

  public static void generateUniqueData(
      List<List<Field>> uniqueKeyFields,
      String tableName,
      int rowCount,
      String domainType,
      List<Table> childTables,
      Map<Tuple, List<String>> concurrentMap,
      DataGenFactory dataGenFactory)
      throws ParseException {
    for (List<Field> list : uniqueKeyFields) {
      for (Field field : list) {
        List<String> data = generateDataForField(field, rowCount, dataGenFactory);
        concurrentMap.put(new Tuple(tableName, field.getColumnName()), data);
        for (Table table : childTables) {
          List<String> columns =
              table
                  .getForigenKeys()
                  .stream()
                  .filter(
                      fk ->
                          fk.getReferenceTable().equals(tableName)
                              && fk.getReferenceColumn().equals(field.getColumnName()))
                  .map(k -> k.getKeyName())
                  .collect(Collectors.toList());
          columns.forEach(
              column -> {
                boolean isUnique =
                    table.getUniqueKeys().stream().anyMatch(c -> (c.getColumns().contains(column)));
                if (isUnique) {
                  concurrentMap.put(new Tuple(table.getTableName(), column), data);
                } else {
                  List<String> depValues = new ArrayList<>();
                  for (int i = 0; i <= table.getRowCount(); i++) {
                    depValues.add(data.get(new Random().nextInt(data.size())));
                  }
                  concurrentMap.put(new Tuple(table.getTableName(), column), depValues);
                }
              });
        }
      }
    }
  }
}
