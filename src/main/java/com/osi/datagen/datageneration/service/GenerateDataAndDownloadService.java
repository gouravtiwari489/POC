package com.osi.datagen.datageneration.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.utils.ZipUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GenerateDataAndDownloadService {
  @Autowired private UserRequestValidator userRequestValidator;

  @Autowired private DataGenerationService dataGenerationService;

  public byte[] generateDataAndDownload(
      String fileType, int rowCount, String domainType, String json, String preferredLocale)
      throws IOException {
    TableList tables = json_to_object(json);
    boolean isPresent = userRequestValidator.isSameRequestPresnt(fileType, tables, rowCount);
    if (!isPresent) {
      dataGenerationService.generateData(tables, fileType, rowCount, domainType, preferredLocale);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    String filePath = ZipUtil.createZipFiles(fileType, user);
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File(filePath)));
    byte[] bytes = IOUtils.toByteArray(isr);
    isr.close();
    return bytes;
  }

  public TableList json_to_object(String updatedMappedData) {
    TableList tables = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      tables = mapper.readValue(updatedMappedData, new TypeReference<TableList>() {});

    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tables;
  }
}
