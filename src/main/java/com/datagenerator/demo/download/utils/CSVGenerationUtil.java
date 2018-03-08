package com.datagenerator.demo.download.utils;

import com.datagenerator.demo.domain.CustomUserDetails;
import java.util.List;

public enum CSVGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {
    ExcelGenerationUtil.INSTANCE.generateData(tableName, excelData, fileType, user);
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user) {
    System.out.println();
  }
}
