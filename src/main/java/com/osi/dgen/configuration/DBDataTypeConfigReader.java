package com.osi.dgen.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DBDataTypeConfigReader {
  public static Map<String, String> map = new HashMap<>();

  static {
    try {
      Resource resource = new ClassPathResource("dbconfig/dbconfig.properties");
      File file = new File(resource.getFile().getPath());
      List<String> lines = FileUtils.readLines(file, "utf-8");
      for (String line : lines) {
        map.put(line.split("=")[1], line.split("=")[0]);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getJavaType(String str) {
    String javaType = null;
    String[] dataTypes = str.split("[\\(\\)]");
    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (entry.getKey().contains(dataTypes[0])) {
        javaType = entry.getValue();
        break;
      }
    }
    return javaType.split("\\.")[1].trim();
  }
}
