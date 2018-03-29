package com.osi.datagen.controller;

import com.osi.datagen.datageneration.service.GenerateDataAndDownloadService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DownloadController {

  @Autowired public GenerateDataAndDownloadService generateDataAndDownloadService;

  @PostMapping("/download")
  public @ResponseBody byte[] generateDataAndDownload(
      @RequestParam(name = "fileType", required = true) String fileType,
      @RequestParam(name = "rowCount", required = true) int rowCount,
      @RequestParam(name = "domainType", required = true) String domainType,
      @RequestParam(name = "tableList", required = true) String tableList,
      @RequestParam(name = "preferredLocale", required = true) String preferredLocale)
      throws IOException {
    log.info("rowCount ::" + rowCount);
    log.info("fileType ::" + fileType);
    log.info("domainType ::" + domainType);
    log.info("tableList ::" + tableList);
    log.info("preferredLocale ::" + preferredLocale);

    return generateDataAndDownloadService.generateDataAndDownload(
        fileType, rowCount, domainType, tableList, preferredLocale);
  }
}
