package com.osi.datagen.service;

import java.io.File;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.osi.datagen.controller.DownloadController;
import com.osi.datagen.domain.CustomUserDetails;

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

public void clearPreExistingUserData(String fileDownloadPath, String updatedMappedData, CustomUserDetails user, List<String> fileTypes) {
		if (user.getMappedData() != null && !user.getMappedData().equals(updatedMappedData)) {
			for (String string : fileTypes) {
				user.getMap().remove(string);
			}
			user.setMappedData(null);
			clearUserData(fileDownloadPath + user.getUsername());
		}
	}
}
