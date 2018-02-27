package com.datagenerator.demo.download.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface GenerateDataInterface {

  public void generateData(String tableName, List<List<String>> excelData, String fileType);

  public void writeToFile(Object obj, String tableName, String fileType)
      throws IOException, FileNotFoundException;
}
