package com.osi.datagen.download.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.osi.datagen.domain.CustomUserDetails;

public interface GenerateDataInterface {

  String fileDownloadPath = "output\\";

  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user);

  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException;
}
