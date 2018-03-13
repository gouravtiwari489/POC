package com.osi.dgen.utils;

import com.osi.dgen.domain.CustomUserDetails;
import com.osi.dgen.download.utils.GenerateDataInterface;
import com.osi.dgen.serviceImpl.RelationalDataExtractor;
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
  private CustomUserDetails user;
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

    this.tableName = tableName;
    this.service = service;
    this.fieldMap = fieldMap;
    this.rowCount = rowCount;
    this.fileType = fileType;
    this.tablFieldMappingeMap = tablFieldMappingeMap;
    this.concurrentMap = concurrentMap;
    this.user = user;
    this.domainType = domainType;
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
              fieldMap,
              tablFieldMappingeMap.get(0),
              rowCount,
              concurrentMap,
              tableName,
              domainType);
      concurrentMap.putAll(
          RelationalDataExtractor.extractdata(tablFieldMappingeMap.get(0), excelData, tableName));
      service.generateData(tableName, excelData, this.fileType, user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
