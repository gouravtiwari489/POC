package com.osi.datagen.filegeneration.util;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.domain.CustomUserDetails;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface GenerateDataInterface {

  String fileDownloadPath = "output";

  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user);

  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException;

  default String createFolders(CustomUserDetails user, String fileType, String tableName) {
    String filePath;
    File userFolder = new File(fileDownloadPath + DasConstants.FILE_SEPRATOR + user.getUsername());
    File file =
        new File(
            fileDownloadPath
                + DasConstants.FILE_SEPRATOR
                + user.getUsername()
                + DasConstants.FILE_SEPRATOR
                + fileType);
    if (!file.exists()) {
      new File(userFolder.getAbsoluteFile().getPath() + DasConstants.FILE_SEPRATOR + fileType)
          .mkdir();
    }
    filePath =
        String.format(
            "%s/%s.%s",
            userFolder.getAbsoluteFile().getPath() + DasConstants.FILE_SEPRATOR + fileType,
            tableName,
            fileType);
    return filePath;
  }
}
