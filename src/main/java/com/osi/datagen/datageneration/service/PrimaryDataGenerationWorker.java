package com.osi.datagen.datageneration.service;

import com.osi.datagen.domain.ForigenKeyConstraint;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.Tuple;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryDataGenerationWorker implements Runnable {
  private Table table;
  private String domainType;
  private int rowCount;
  private Map<Tuple, List<String>> concurrentMap;
  private List<Table> childTables;

  public PrimaryDataGenerationWorker(int rowCount, String fileType, String domainType) {}

  public PrimaryDataGenerationWorker(
      Table table,
      String domainType,
      int rowCount,
      List<Table> childTables,
      Map<Tuple, List<String>> concurrentMap) {
    this.table = table;
    this.domainType = domainType;
    this.rowCount = rowCount;
    this.concurrentMap = concurrentMap;
    this.childTables = childTables;
  }

  @Override
  public void run() {

    try {
      if (table.getPrimaryKeyFields() != null) {
        generatePrimaryKeyData();
      }
      if (table.getUniqueKeyFields() != null) {
        generateUniqueKeyData();
      }
      log.info("unique field generation completed for table" + table.getTableName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generateUniqueKeyData() throws ParseException {

    GenerateSampleDataUtil.generateUniqueData(
        table.getUniqueKeyFields(),
        table.getTableName(),
        rowCount,
        domainType,
        childTables,
        concurrentMap);
    generateSelfDependencyData();
  }

  private void generateSelfDependencyData() {
    if (table.getSelfDependencies() != null) {
      List<ForigenKeyConstraint> fks = table.getSelfDependencies();
      fks.forEach(
          fk -> {
            List<String> values =
                concurrentMap.get(new Tuple(table.getTableName(), fk.getKeyName()));
            List<String> depValues = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
              depValues.add(values.get(new Random().nextInt(values.size())));
            }
            concurrentMap.put(new Tuple(table.getTableName(), fk.getKeyName()), depValues);
          });
    }
  }

  private void generatePrimaryKeyData() throws ParseException {

    GenerateSampleDataUtil.generatePrimaryKeyData(
        table.getPrimaryKeyFields(),
        table.getTableName(),
        rowCount,
        domainType,
        childTables,
        concurrentMap);
    generateSelfDependencyData();
  }
}
