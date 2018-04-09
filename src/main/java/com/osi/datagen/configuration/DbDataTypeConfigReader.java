package com.osi.datagen.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
public class DbDataTypeConfigReader {
  public static Map<String, String> map = new HashMap<>();

  static {
    try {
      Resource resource = new ClassPathResource("dbconfig/dbconfig.properties");
      File file = new File(resource.getFile().getPath());
      List<String> lines = FileUtils.readLines(file, "utf-8");

      for (String line : lines) {
        StringTokenizer dataType = new StringTokenizer(line, "=");
        String firstToken = dataType.nextToken().trim();
        StringTokenizer dataTypes = new StringTokenizer(dataType.nextToken(), ",");
        while (dataTypes.hasMoreElements()) {
          map.put(dataTypes.nextElement().toString().trim().toUpperCase(), firstToken);
        }
      }

    } catch (Exception e) {
      log.error("exception while reading dbconfig.properties ", e);
    }
  }

  public static String getJavaType(String str) {
    return map.get(str);
  }
}
