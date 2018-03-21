package com.osi.datagen.filegeneration.util;

import java.util.List;
import com.osi.datagen.domain.CustomUserDetails;

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
