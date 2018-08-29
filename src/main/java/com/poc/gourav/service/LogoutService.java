package com.poc.gourav.service;

import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.poc.gourav.domain.CustomUserDetails;

@Slf4j
@Service
public class LogoutService {

  public void clearUserData(String userFolderName) {
    File resource = new File(userFolderName);

    if (resource.exists()) {
      File directory;
      directory = resource.getAbsoluteFile();
      File[] fileList = directory.listFiles();
      for (File file : fileList) {
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

  /*public void clearPreExistingUserData(
      String fileDownloadPath, TableList tables, CustomUserDetails user, List<String> fileTypes) {
    if (user.getTables() != null && !user.getTables().equals(tables)) {
      for (String string : fileTypes) {
        user.getMap().remove(string);
      }
      user.setTables(null);
      clearUserData(fileDownloadPath + user.getUsername());
    }
  }*/
}
