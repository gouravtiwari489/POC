package com.datagenerator.das.serviceImpl;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutService {

  public void clearUserData(String userFolderName) {
    Resource resource = new ClassPathResource(userFolderName);

    if (resource.exists()) {
      File directory;
      try {
        directory = resource.getFile();
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
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
