package com.osi.datagen.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osi.datagen.component.LoadFileGenerationObjects;
import com.osi.datagen.domain.CustomUserDetails;
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

  //  @Autowired private CustomTokenConverter customTokenConverter;
  @Autowired LoadFileGenerationObjects fileGenObj;

  @SuppressWarnings("unchecked")
  public void generateData(
      String updatedMappedData, String fileType, int rowCount, String domainType)
      throws IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    /*tablFieldMappingeMap =
    (List<LinkedHashMap<String, LinkedHashMap<String, String>>>)
        customTokenConverter.getAdditionalInfo("mappedTables");*/
    tablFieldMappingeMap = user.getMappedTables();

    /*Map<Integer, List<String>> tablesMap =
    (Map<Integer, List<String>>) customTokenConverter.getAdditionalInfo("orderedFKList");*/
    Map<Integer, List<String>> tablesMap = user.getOrderedFKListMap();
    log.info("tablFieldMappingeMap values after getting from context", tablFieldMappingeMap);
    log.info("tablesMap values after getting from context", tablesMap);
    GenerateDataInterface service = fileGenObj.getGenDataServiceMap().get(fileType);
    Map<String, LinkedHashMap<String, String>> map = json_to_map(updatedMappedData);
    threadService(tablesMap, fileType, rowCount, map, service, domainType);
    //    customTokenConverter.setAdditionalInfo("updatedMappedData", updatedMappedData);
  }

  public void threadService(
      Map<Integer, List<String>> tablesMap,
      String fileType,
      int rowCount,
      Map<String, LinkedHashMap<String, String>> map,
      GenerateDataInterface service,
      String domainType)
      throws IOException {
    try {
      /*CustomUserDetails user =
      (CustomUserDetails) customTokenConverter.getAdditionalInfo("CurrentUser");*/
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
      Map<String, List<String>> concurrentMap = new ConcurrentHashMap<>();
      for (Map.Entry<Integer, List<String>> entry : tablesMap.entrySet()) {
        log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        List<String> tablesList = entry.getValue();
        ExecutorService executor = Executors.newFixedThreadPool(tablesList.size());
        for (String tableName : tablesList) {
          Runnable dataGenerationWorker =
              new DataGenerationWorker(
                  tableName,
                  map.get(tableName),
                  rowCount,
                  fileType,
                  tablFieldMappingeMap,
                  concurrentMap,
                  service,
                  user,
                  domainType);
          executor.execute(dataGenerationWorker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
      }
      //      customTokenConverter.setAdditionalInfo(fileType, String.valueOf(rowCount));
    } catch (Exception ex) {
      log.error("Error wrting to file", ex);
      ex.printStackTrace();
    }
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
