package com.datagenerator.demo.download.utils;

import com.datagenerator.demo.domain.CustomUserDetails;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface GenerateDataInterface {

  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user);

  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException;
}
