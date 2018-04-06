package com.osi.datagen.datageneration.service;

import com.osi.datagen.datagenerators.DataGenFactory;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.domain.Tuple;
import com.osi.datagen.filegeneration.util.GenerateDataInterface;
import com.osi.datagen.filegeneration.util.LoadFileGenerationObjects;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataGenerationService {
  
  
  @Value("${parent.table.rowcount.percentage}")
  public String rowCountPercentage;

  @Autowired LoadFileGenerationObjects fileGenObj;

  public void generateData(TableList tables, String fileType, int rowCount, String domainType, String preferredLocale)
      throws IOException {
    setTablesRowcount(tables,rowCount);
    Map<Tuple, List<String>> concurrentMap = new ConcurrentHashMap<>();
    DataGenFactory dgf=new DataGenFactory(domainType, preferredLocale);
    threadServicePrimaryDataGeneration(tables, fileType, domainType, concurrentMap,dgf);
    GenerateDataInterface service = fileGenObj.getGenDataServiceMap().get(fileType);
    threadService(tables, fileType, domainType, concurrentMap, service,preferredLocale,dgf);
  }
  private void setTablesRowcount(TableList tables, int rowCount) {
    for (Table table:tables) {
      if( table.getForigenKeys()==null || table.getForigenKeys().isEmpty()){
        double rowsPercentage=Double.parseDouble(rowCountPercentage);
        int actualRowCount=(int)(rowCount*rowsPercentage);
        table.setRowCount(actualRowCount);
        List<Table> childTables=tables.getChildTables(table);
        for (Table childTable : childTables) {
          List<String> columnNames=childTable.getForigenKeys()
          .stream()
          .filter(fk->fk.getReferenceTable().equalsIgnoreCase(table.getTableName()))
          .map(fk->fk.getKeyName())
          .collect(Collectors.toList());
          boolean isUnique=childTable.getUniqueKeys().stream().anyMatch(c->(c.getColumns().containsAll(columnNames) && c.getColumns().size() == columnNames.size()));
          if(isUnique){
            childTable.setRowCount(actualRowCount);
          }else{
            if(table.getRowCount()==0){
            childTable.setRowCount(rowCount);
            }
          }
        }
        
      }else{
        if(table.getRowCount()==0){
        table.setRowCount(rowCount);
        }
      }
      
    }
  }
  private void threadServicePrimaryDataGeneration(
      TableList tables,
      String fileType,
      String domainType,
      Map<Tuple, List<String>> concurrentMap, DataGenFactory dgf) {
    for (Table table : tables.getTables()) {
      ExecutorService executor = Executors.newFixedThreadPool(tables.getTables().size());
      Runnable dataGenerationWorker =
          new PrimaryDataGenerationWorker(
              table, domainType, table.getRowCount(), tables.getChildTables(table), concurrentMap,dgf);
      executor.execute(dataGenerationWorker);
      executor.shutdown();
      while (!executor.isTerminated()) {}
    }
  }

  
  public void threadService(
      TableList tableList,
      String fileType,
      String domainType,
      Map<Tuple, List<String>> concurrentMap,
      GenerateDataInterface service, String preferredLocale, DataGenFactory dgf)
      throws IOException {
    log.info("creating thread pool");
    ExecutorService executor = Executors.newFixedThreadPool(5);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    for (Table table : tableList.getTables()) {
      Runnable dataGenerationWorker =
          new DataGenerationWorker(
              table, table.getRowCount(), fileType, concurrentMap, domainType, service, user,preferredLocale,dgf);
      executor.execute(dataGenerationWorker);
    }
    executor.shutdown();
    while (!executor.isTerminated()) {}
  }
}
