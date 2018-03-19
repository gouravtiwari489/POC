package com.osi.datagen.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osi.datagen.component.LoadFileGenerationObjects;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.download.utils.GenerateDataInterface;
import com.osi.datagen.utils.DataGenerationWorker;
import java.io.IOException;
import java.util.LinkedHashMap;
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

  public static List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap =
      null;

  @Autowired LoadFileGenerationObjects fileGenObj;

  @SuppressWarnings("unchecked")
  public void generateData(
      TableList tables, String fileType, int rowCount, String domainType)
      throws IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    tablFieldMappingeMap = user.getMappedTables();
    Map<Integer, List<String>> tablesMap = user.getOrderedFKListMap();
    log.info("tablFieldMappingeMap values after getting from context", tablFieldMappingeMap);
    log.info("tablesMap values after getting from context", tablesMap);
   // GenerateDataInterface service = fileGenObj.getGenDataServiceMap().get(fileType);
    threadService(tables, fileType, rowCount, domainType);
  }

  public void threadService(
      TableList tableList,
      String fileType,
      int rowCount,
      String domainType)
      throws IOException {
     
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
      Map<String, List<String>> concurrentMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(tableList.getTables().size());
        for (Table table : tableList.getTables()) {
          Runnable dataGenerationWorker =
              new DataGenerationWorker(
                  table,
                  rowCount,
                  fileType,
                  concurrentMap,
                  domainType);
          executor.execute(dataGenerationWorker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
  
  }

  public Map<String, LinkedHashMap<String, String>> json_to_map(String updatedMappedData) {
    Map<String, LinkedHashMap<String, String>> map = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      map =
          mapper.readValue(
              updatedMappedData,
              new TypeReference<Map<String, LinkedHashMap<String, String>>>() {});

    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }
}
