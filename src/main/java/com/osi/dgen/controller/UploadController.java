package com.osi.dgen.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.osi.dgen.exception.DependencyException;
import com.osi.dgen.service.DataGenerationService;
import com.osi.dgen.service.LogoutService;
import com.osi.dgen.service.SQLFileReaderService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class UploadController {

  @Autowired SQLFileReaderService sqlFileReadService;

  @Autowired DataGenerationService dataGenerationService;

  //  @Autowired CustomTokenConverter customTokenConverter;

  @Autowired LogoutService logoutService;

  @Value("${file.download.path}")
  private String fileDownloadPath;
  
  @Value("${file.types}")
  private String[] allFileTypes;

  @PostMapping("/upload")
  public ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>> uploadFile(
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

}
