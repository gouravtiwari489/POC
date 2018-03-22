package com.osi.datagen.datageneration.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.Tuple;

public class PrimaryDataGenerationWorker implements Runnable {
  private Table table;
  private String domainType;
  private int rowCount;
  private Map<Tuple, List<String>> concurrentMap;
  private List<Table> childTables;
  
  public PrimaryDataGenerationWorker(
      int rowCount,
      String fileType,
      String domainType) {
   
  }
  public PrimaryDataGenerationWorker(Table table,
      String domainType, int rowCount, List<Table> childTables, Map<Tuple, List<String>> concurrentMap) {
    this.table=table;
    this.domainType=domainType;
    this.rowCount=rowCount;
    this.concurrentMap=concurrentMap;
    this.childTables=childTables;
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
        GenerateSampleDataUtil.generateUniqueData(table.getUniqueKeyFields(),table.getTableName(),
            rowCount,
            domainType,childTables,concurrentMap);
        
        
        
    
  }
  private void generatePrimaryKeyData() throws ParseException {
   
        GenerateSampleDataUtil.generatePrimaryKeyData(table.getPrimaryKeyFields(),table.getTableName(), rowCount,domainType,childTables,concurrentMap);
    
  }
}
