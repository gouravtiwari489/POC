package com.datagenerator.demo.controller;

import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.serviceImpl.DataGenerationService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

  @Autowired private HttpServletResponse response;

  @PostMapping("/upload")
  public ResponseEntity<List<LinkedHashMap<String, LinkedHashMap<String, String>>>> uploadProfile(
      @RequestParam(name = "file", required = true) MultipartFile multipartFile,
      @RequestParam(name = "domainType", required = true) String domainType,
      @RequestParam(name = "dependencyToggle", required = true) String dependencyToggle)
      throws Exception {
    boolean dependencyCheck = Boolean.parseBoolean(dependencyToggle);
    log.info(
        "@@@@@@@@@@@@@@@@@ dependencyToggle   "
            + dependencyCheck
            + "   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = null;
    try {
      list = sqlFileReadService.readSQLfile(multipartFile, domainType,dependencyCheck);
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }

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
    dataGenerationService.generateData(updatedMappedData, fileType, rowCount);
    String generatedFileName =
        fileType.equals("xlsx")
            ? "output/DAS.zip"
            : fileType.equals("csv")
                ? "output/DAS.zip"
                : fileType.equals("sql") ? "output/DAS.sql" : "output/DAS.xml";
    Resource resource = new ClassPathResource(generatedFileName);
    File file = resource.getFile();
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(file));
    return IOUtils.toByteArray(isr);
  }
}
