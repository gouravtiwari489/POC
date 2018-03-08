package com.datagenerator.demo.controller;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.datagenerator.demo.exception.DependencyException;
import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.serviceImpl.DataGenerationService;
import com.datagenerator.demo.serviceImpl.LogoutService;
import com.datagenerator.demo.utils.CustomTokenConverter;
import com.datagenerator.demo.utils.ZipUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

  @Autowired SQLFileReadService sqlFileReadService;

  @Autowired DataGenerationService dataGenerationService;

  @Autowired CustomTokenConverter customTokenConverter;

  @Autowired LogoutService logoutService;

  List<String> fileTypes = new ArrayList<>(Arrays.asList("csv", "xlsx", "xml", "sql", "json"));

  @PostMapping("/upload")
  public ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>> uploadProfile(
      @RequestParam(name = "file", required = true) MultipartFile multipartFile,
      @RequestParam(name = "domainType", required = true) String domainType,
      @RequestParam(name = "dependencyToggle", required = true) String dependencyToggle)
      throws DependencyException, Exception {
    boolean dependencyCheck = Boolean.parseBoolean(dependencyToggle);
    log.info(
        "@@@@@@@@@@@@@@@@@ dependencyToggle   "
            + dependencyCheck
            + "   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = null;
    //try {
    list = sqlFileReadService.readSQLfile(multipartFile, domainType, dependencyCheck);
    //} catch (Exception ex) {
    //throw new Exception(ex.getMessage());
    //}

    return new ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>>(
        list, HttpStatus.OK);
  }

  @PostMapping("/download")
  public @ResponseBody byte[] downloadExcelFile(
      @RequestParam(name = "fileType", required = true) String fileType,
      @RequestParam(name = "rowCount", required = true) int rowCount,
      @RequestParam(name = "updatedMappedData", required = true) String updatedMappedData)
      throws Exception {
    log.info("@@@@@@@@@@@@@@@@@ rowCount   " + rowCount + "   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    CustomUserDetails user =
        (CustomUserDetails) customTokenConverter.getAdditionalInfo("CurrentUser");
    if (customTokenConverter.getAdditionalInfo(fileType) == null
        || !customTokenConverter.getAdditionalInfo(fileType).equals(String.valueOf((rowCount)))
        || customTokenConverter.getAdditionalInfo("updatedMappedData") == null
        || !customTokenConverter.getAdditionalInfo("updatedMappedData").equals(updatedMappedData)) {
      if (customTokenConverter.getAdditionalInfo("updatedMappedData") != null) {
        if (!customTokenConverter
            .getAdditionalInfo("updatedMappedData")
            .equals(updatedMappedData)) {
          for (String string : fileTypes) {
            customTokenConverter.setAdditionalInfo(string, null);
          }
          customTokenConverter.setAdditionalInfo("updatedMappedData", null);
          logoutService.clearUserData("output/" + user.getUsername());
        }
      }
      dataGenerationService.generateData(updatedMappedData, fileType, rowCount);
    }
    String filePath = ZipUtil.createZipFiles(fileType, user);
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File(filePath)));
    byte[] bytes = IOUtils.toByteArray(isr);
    isr.close();
    return bytes;
  }
}
