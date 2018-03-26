package com.osi.datagen.datageneration.service;

import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.Tuple;
import com.osi.datagen.filegeneration.util.GenerateDataInterface;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {

  private Table table;
  private int rowCount;
  private String fileType;
  private Map<Tuple, List<String>> concurrentMap;
  private String domainType;
  private GenerateDataInterface service;
  private CustomUserDetails user;
  private String preferredLocale;


  public DataGenerationWorker(
      Table table,
      int rowCount,
      String fileType,
      Map<Tuple, List<String>> concurrentMap,
      String domainType,
      GenerateDataInterface service,
      CustomUserDetails user, String preferredLocale) {
    
    this.table = table;
    this.rowCount = rowCount;
    this.fileType = fileType;
    this.concurrentMap = concurrentMap;
    this.domainType = domainType;
    this.service = service;
    this.user = user;
    this.preferredLocale=preferredLocale;
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
      long start = System.currentTimeMillis();
      List<List<String>> excelData =
          GenerateSampleDataUtil.generateData(table, rowCount, concurrentMap, domainType,preferredLocale);
      long end = System.currentTimeMillis();
      log.info("time taken for data genration " + table.getTableName() + (end - start));
      service.generateData(table.getTableName(), excelData, this.fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
