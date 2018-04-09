package com.osi.datagen.filegeneration.util;

import static com.osi.datagen.datageneration.service.DataGenUtil.removeSingleQuotes;

import com.google.gson.Gson;
import com.osi.datagen.domain.CustomUserDetails;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum JsonGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {
    Map<String, String> jsonMap = null;
    List<String> headers = excelData.get(0);
    List<Map<String, String>> finalMap = new ArrayList<>();
    for (int i = 1; i < excelData.size(); i++) {
      jsonMap = new LinkedHashMap<>();
      List<String> data = excelData.get(i);
      for (int j = 0; j < data.size(); j++) {
        jsonMap.put(headers.get(j), removeSingleQuotes(data.get(j)));
      }
      finalMap.add(jsonMap);
    }
    Gson gs = new Gson();
    String gss = gs.toJson(finalMap);
    excelData.clear();
    try {
      writeToFile(gss, tableName, fileType, user);
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    String filePath = createFolders(user, fileType, tableName);
    BufferedWriter jsonFile = new BufferedWriter(new FileWriter(filePath));

    jsonFile.write((String) obj);
    jsonFile.flush();
    jsonFile.close();
  }
}
