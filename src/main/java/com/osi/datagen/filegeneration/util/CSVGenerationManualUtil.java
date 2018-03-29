package com.osi.datagen.filegeneration.util;

import static com.osi.datagen.datageneration.service.DataGenUtil.removeSingleQuotes;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.domain.CustomUserDetails;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public enum CSVGenerationManualUtil implements GenerateDataInterface {
  INSTANCE;

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {

    StringBuilder csvData = new StringBuilder();
    for (int i = 0; i < excelData.size(); i++) {
      List<String> data = excelData.get(i);
      String commaSeparatedRow =
          data.stream()
              .map(j -> removeSingleQuotes(j))
              .collect(Collectors.joining(DasConstants.COMMA_SEPRATOR));
      csvData.append(commaSeparatedRow + DasConstants.NEWLINE_CHARACTER);
    }

    try {
      writeToFile(csvData.toString(), tableName, fileType, user);
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
    String filePath;
    File resource = new File(fileDownloadPath + user.getUsername());
    if (!resource.exists()) {
      new File(fileDownloadPath + user.getUsername()).mkdir();
    }
    File file = new File(resource.getAbsoluteFile().getPath() + "/" + fileType);
    if (!file.exists()) {
      file.mkdir();
    }
    filePath =
        String.format(
            "%s\\%s.%s",
            resource.getAbsoluteFile().getPath() + "\\" + fileType, tableName, fileType);
    BufferedWriter jsonFile = new BufferedWriter(new FileWriter(filePath));

    jsonFile.write((String) obj);
    jsonFile.flush();
    jsonFile.close();
  }
}
