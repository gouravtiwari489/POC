package com.datagenerator.demo.utils;

import com.datagenerator.demo.download.utils.GenerateDataInterface;
import com.datagenerator.demo.serviceImpl.RelationalDataExtractor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {

  private String tableName;
  private LinkedHashMap<String, String> fieldMap;
  private int rowCount;
  private GenerateDataInterface service;
  private String fileType;
  private List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap;
  private Map<String, List<String>> concurrentMap;

  public DataGenerationWorker(
      String tableName,
      LinkedHashMap<String, String> fieldMap,
      int rowCount,
      String fileType,
      List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap,
      Map<String, List<String>> concurrentMap,
      GenerateDataInterface service) {

    this.tableName = tableName;
    this.service = service;
    this.fieldMap = fieldMap;
    this.rowCount = rowCount;
    this.fileType = fileType;
    this.tablFieldMappingeMap = tablFieldMappingeMap;
    this.concurrentMap = concurrentMap;
  }

  @Override
  public void run() {

    try {
      generateData(tableName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("data generation completed for " + tableName);
  }

  private void generateData(String tableName) {
    try {

      List<List<String>> excelData =
          GenerateSampleDataUtil.generateData(
              fieldMap, tablFieldMappingeMap.get(0), rowCount, concurrentMap, tableName);
      concurrentMap.putAll(
          RelationalDataExtractor.extractdata(tablFieldMappingeMap.get(0), excelData, tableName));
      service.generateData(tableName, excelData, this.fileType);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
