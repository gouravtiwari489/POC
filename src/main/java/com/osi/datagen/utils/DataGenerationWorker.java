package com.osi.datagen.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Table;
import com.osi.datagen.download.utils.GenerateDataInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {
  
  private Table table;
  private int rowCount;
  private String fileType;
  private Map<String, List<String>> concurrentMap;
  private String domainType;

  public DataGenerationWorker(
      String tableName,
      LinkedHashMap<String, String> fieldMap,
      int rowCount,
      String fileType,
      List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap,
      Map<String, List<String>> concurrentMap,
      GenerateDataInterface service,
      CustomUserDetails user,
      String domainType) {
   
  }

  public DataGenerationWorker(Table table, int rowCount, String fileType,
      Map<String, List<String>> concurrentMap,  String domainType) {
        this.table=table;
        this.rowCount = rowCount;
        this.fileType = fileType;
        this.concurrentMap = concurrentMap;
        this.domainType = domainType;
  }

  @Override
  public void run() {

    try {
      generateData(table);
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("data generation completed for " + table.getTableName());
  }

  private void generateData(Table table) {
    try {

      List<List<String>> excelData =
          GenerateSampleDataUtil.generateData(table,
              rowCount,
              concurrentMap,
              domainType);
      concurrentMap.putAll(
          RelationalDataExtractor.extractdata(tablFieldMappingeMap.get(0), excelData, tableName));
      service.generateData(tableName, excelData, this.fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
