package com.datagenerator.demo.download.utils;

import static com.datagenerator.demo.utils.DataGenUtil.removeSingleQuotes;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public enum JSONGenerationUtil implements GenerateDataInterface {
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
    try {
      writeToFile(gss, tableName, fileType, user);
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    String filePath;
    Resource resource = new ClassPathResource("output\\" + user.getUsername());
    if (!resource.exists()) {
      new File("bin\\output\\" + user.getUsername()).mkdir();
    }
    File file = new File(resource.getFile().getPath() + "/" + fileType);
    if (!file.exists()) {
      file.mkdir();
    }
    filePath =
        String.format(
            "%s\\%s.%s", resource.getFile().getPath() + "\\" + fileType, tableName, fileType);
    BufferedWriter jsonFile = new BufferedWriter(new FileWriter(filePath));

    jsonFile.write((String) obj);
    jsonFile.flush();
    jsonFile.close();
  }
}
