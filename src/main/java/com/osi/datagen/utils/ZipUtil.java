package com.osi.datagen.utils;

import com.osi.datagen.domain.CustomUserDetails;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZipUtil {

  private static String fileDownloadPath;

  @Value("${file.download.path}")
  public void setPath(String path) {
    fileDownloadPath = path;
  }

  public static String createZipFiles(String fileType, CustomUserDetails currentUser)
      throws IOException {

    List<String> filePaths = new ArrayList<>();
    File resource = new File(fileDownloadPath + currentUser.getUsername() + "\\" + fileType);
    String zipFilePath =
        String.format("%s\\%s.%s", resource.getAbsoluteFile().getPath(), "DAS", "zip");
    File folder = new File(resource.getAbsoluteFile().getPath());
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()
          && !listOfFiles[i].getName().endsWith("zip")
          && listOfFiles[i].getName().endsWith(fileType)) {
        filePaths.add(listOfFiles[i].toString());
      }
    }
    zipFiles(filePaths, zipFilePath);
    return zipFilePath;
  }

  private static void zipFiles(List<String> files, String zipPath) throws IOException {

    FileOutputStream fos = null;
    ZipOutputStream zipOut = null;
    FileInputStream fis = null;
    try {
      fos = new FileOutputStream(zipPath);
      zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
      for (String filePath : files) {
        File input = new File(filePath);
        fis = new FileInputStream(input);
        ZipEntry ze = new ZipEntry(input.getName());
        zipOut.putNextEntry(ze);
        byte[] tmp = new byte[4 * 1024];
        int size = 0;
        while ((size = fis.read(tmp)) != -1) {
          zipOut.write(tmp, 0, size);
        }
        zipOut.closeEntry();
        fis.close();
      }
      zipOut.close();
    } catch (FileNotFoundException e) {
      log.error(e.getMessage());
      throw new FileNotFoundException();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new IOException();
    } finally {
      try {
        if (fos != null) {
          fos.flush();
          fos.close();
        }
      } catch (Exception ex) {
        log.error(ex.getMessage());
        throw new IOException();
      }
    }
  }
}
