package com.datagenerator.demo.serviceImpl;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutService {

  public void clearUserData(String userFolderName) {
    File resource = new File(userFolderName);

    if (resource.exists()) {
      File directory;
      directory = resource.getAbsoluteFile();
      File[] fList = directory.listFiles();
      for (File file : fList) {
        if (file.isFile()) {
          log.info("output/" + userFolderName + "/" + file.getName() + " is Deleted");
          file.delete();
        } else if (file.isDirectory()) {
          clearUserData(userFolderName + "/" + file.getName());
        }
      }
      directory.delete();
    }
  }
}
