package com.osi.datagen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.exception.DependencyException;
import com.osi.datagen.parsing.service.SQLFileReaderService;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class UploadController {

  @Autowired SQLFileReaderService sqlFileReadService;

  @PostMapping("/upload")
  public ResponseEntity<TableList> uploadFile(
      @RequestParam(name = "file", required = true) MultipartFile multipartFile,
      @RequestParam(name = "domainType", required = true) String domainType,
      @RequestParam(name = "dependencyToggle", required = true) String dependencyToggle)
      throws DependencyException, Exception {
    boolean dependencyCheck = Boolean.parseBoolean(dependencyToggle);
    log.info(
        " dependencyToggle   "
            + dependencyCheck);
    TableList tableList = null;
    tableList = sqlFileReadService.readSQLfile(multipartFile, domainType, dependencyCheck);
    return new ResponseEntity<TableList>(
        tableList, HttpStatus.OK);
  }

}
