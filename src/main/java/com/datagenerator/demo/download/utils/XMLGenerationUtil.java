package com.datagenerator.demo.download.utils;

import com.datagenerator.demo.domain.CustomUserDetails;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public enum XMLGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {}

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    // TODO Auto-generated method stub

  }
}
