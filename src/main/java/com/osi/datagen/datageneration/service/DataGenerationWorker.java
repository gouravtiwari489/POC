package com.osi.datagen.datageneration.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Table;
import com.osi.datagen.filegeneration.util.GenerateDataInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {
  
  private Table table;
  private int rowCount;
  private String fileType;
  private Map<String, List<String>> concurrentMap;
  private String domainType;
  private GenerateDataInterface service;

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
      Map<String, List<String>> concurrentMap,  String domainType,GenerateDataInterface service) {
        this.table=table;
        this.rowCount = rowCount;
        this.fileType = fileType;
        this.concurrentMap = concurrentMap;
        this.domainType = domainType;
        this.service=service;
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
     /* concurrentMap.putAll(
          RelationalDataExtractor.extractdata(tablFieldMappingeMap.get(0), excelData, tableName));*/
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
      service.generateData(table.getTableName(), excelData, this.fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
