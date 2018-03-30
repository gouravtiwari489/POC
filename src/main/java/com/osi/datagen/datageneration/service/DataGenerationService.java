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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataGenerationService {

  @Autowired LoadFileGenerationObjects fileGenObj;

  public void generateData(TableList tables, String fileType, int rowCount, String domainType, String preferredLocale)
      throws IOException {
    Map<Tuple, List<String>> concurrentMap = new ConcurrentHashMap<>();
    DataGenFactory dgf=new DataGenFactory(domainType, preferredLocale);
    threadServicePrimaryDataGeneration(tables, fileType, rowCount, domainType, concurrentMap,dgf);
    GenerateDataInterface service = fileGenObj.getGenDataServiceMap().get(fileType);
    threadService(tables, fileType, rowCount, domainType, concurrentMap, service,preferredLocale,dgf);
  }

  private void threadServicePrimaryDataGeneration(
      TableList tables,
      String fileType,
      int rowCount,
      String domainType,
      Map<Tuple, List<String>> concurrentMap, DataGenFactory dgf) {
    for (Table table : tables.getTables()) {
      ExecutorService executor = Executors.newFixedThreadPool(tables.getTables().size());
      Runnable dataGenerationWorker =
          new PrimaryDataGenerationWorker(
              table, domainType, rowCount, tables.getChildTables(table), concurrentMap,dgf);
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
              table, rowCount, fileType, concurrentMap, domainType, service, user,preferredLocale,dgf);
      executor.execute(dataGenerationWorker);
    }
    executor.shutdown();
    while (!executor.isTerminated()) {}
  }
}
