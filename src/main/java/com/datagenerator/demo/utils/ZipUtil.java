package com.datagenerator.demo.utils;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
public class ZipUtil {

  public static String createZipFiles(String fileType) throws IOException {

    List<String> filePaths = new ArrayList<>();
    Resource resource = new ClassPathResource("output");
    String zipFilePath = String.format("%s\\%s.%s", resource.getFile().getPath(), "DAS", "zip");
    File folder = new File(resource.getFile().getPath());
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

  private static void zipFiles(List<String> files, String zipPath) {

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
        //input.delete(); uncomment when we have implementation of diff folder for each file type
      }
      zipOut.close();
    } catch (FileNotFoundException e) {
      log.error(e.getMessage());
    } catch (IOException e) {
      log.error(e.getMessage());
    } finally {
      try {
        if (fos != null) {
          fos.flush();
          fos.close();
        }
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }
  }
}
