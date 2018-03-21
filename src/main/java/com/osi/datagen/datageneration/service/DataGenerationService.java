package com.osi.datagen.datageneration.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.filegeneration.util.GenerateDataInterface;
import com.osi.datagen.filegeneration.util.LoadFileGenerationObjects;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataGenerationService {

  @Autowired LoadFileGenerationObjects fileGenObj;

  public void generateData(
      TableList tables, String fileType, int rowCount, String domainType)
      throws IOException {
    Map<String, List<String>> concurrentMap = new ConcurrentHashMap<>();
    threadServicePrimaryDataGeneration(tables, fileType, rowCount, domainType,concurrentMap);
    GenerateDataInterface service = fileGenObj.getGenDataServiceMap().get(fileType);
    threadService(tables, fileType, rowCount, domainType,concurrentMap,service);
  }

  private void threadServicePrimaryDataGeneration(TableList tables,
      String fileType,
      int rowCount,
      String domainType,Map<String, List<String>> concurrentMap) {
    for (Table table : tables.getTables()) {
     
      List<Field> primarkeyFields=table.getPrimaryKeyFields();
      List<List<Field>> uniqueKeyFields=table.getUniqueKeyFields();
      
      ExecutorService executor = Executors.newFixedThreadPool(tables.getTables().size());
        Runnable dataGenerationWorker =
            new PrimaryDataGenerationWorker(primarkeyFields,
                uniqueKeyFields,
                domainType,rowCount,concurrentMap);
        executor.execute(dataGenerationWorker);
      executor.shutdown();
      while (!executor.isTerminated()) {}
      
      
    }
    
  }

  public void threadService(
      TableList tableList,
      String fileType,
      int rowCount,
      String domainType,
      Map<String, List<String>> concurrentMap,GenerateDataInterface service)
      throws IOException {
      log.info("creating thread pool");
        ExecutorService executor = Executors.newFixedThreadPool(tableList.getTables().size());
        for (Table table : tableList.getTables()) {
          Runnable dataGenerationWorker =
              new DataGenerationWorker(
                  table,
                  rowCount,
                  fileType,
                  concurrentMap,
                  domainType,service);
          executor.execute(dataGenerationWorker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
  
  }

 
}
