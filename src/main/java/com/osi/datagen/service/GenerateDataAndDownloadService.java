package com.osi.datagen.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.utils.ZipUtil;

@Service
public class GenerateDataAndDownloadService {
  @Autowired
  private UserRequestValidator userRequestValidator;
  
  @Autowired
  private DataGenerationService dataGenerationService;
  
  public  byte[] generateDataAndDownload(String fileType, int rowCount, String domainType, TableList tables) throws IOException {
    
    boolean isRequestAlreadyPresent=userRequestValidator.IsSameRequestPresnt(fileType, tables, rowCount);
    if(!isRequestAlreadyPresent){
      dataGenerationService.generateData(tables, fileType, rowCount, domainType);
    }
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    String filePath = ZipUtil.createZipFiles(fileType, user);
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File(filePath)));
    byte[] bytes = IOUtils.toByteArray(isr);
    isr.close();
    return bytes;
    
  }

}
