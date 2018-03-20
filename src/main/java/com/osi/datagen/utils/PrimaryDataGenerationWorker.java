package com.osi.datagen.utils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import com.osi.datagen.domain.Field;

public class PrimaryDataGenerationWorker implements Runnable {
  private List<Field> primaryKeyFields;
  private List<List<Field>> uniqueKeyFields;
  private String domainType;
  private int rowCount;
  private Map<String, List<String>> concurrentMap;
  
  public PrimaryDataGenerationWorker(
      int rowCount,
      String fileType,
      String domainType) {
   
  }
  public PrimaryDataGenerationWorker(List<Field> primarkeyFields, List<List<Field>> uniqueKeyFields,
      String domainType, int rowCount, Map<String, List<String>> concurrentMap) {
    this.primaryKeyFields=primarkeyFields;
    this.uniqueKeyFields=uniqueKeyFields;
    this.domainType=domainType;
    this.rowCount=rowCount;
    this.concurrentMap=concurrentMap;
  }

  @Override
  public void run() {

    try {
      generatePrimaryKeyData();
      generateUniqueKeyData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generateUniqueKeyData() throws ParseException {
        GenerateSampleDataUtil.generateUniquData(uniqueKeyFields,
            rowCount,
            domainType,concurrentMap);
    
  }
  private void generatePrimaryKeyData() throws ParseException {
   
        GenerateSampleDataUtil.generateDataForColumn(primaryKeyFields, rowCount,domainType,concurrentMap);
    
  }
}
