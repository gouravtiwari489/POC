package com.datagenerator.demo.datagen;

import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class StringDataGenerator implements IDataGenerator {
  public static Map<String, List<String>> map = new HashMap<>();
  private int begin = 1000;

  static {
    try {
      Resource resource = new ClassPathResource("datasets");
      resource.getFile().getPath();
      File[] files = new File(resource.getFile().getPath()).listFiles();

      for (File file : files) {
        List<String> lines = FileUtils.readLines(file, "utf-8");
        map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String generateData(Field field) {
    if (field.isPrimaryKey()) {
      return generateUniqueData(field);
    } else {
      return generateRandomData(field);
    }
  }

  private String generateUniqueData(Field field) {
    begin++;
    return field.getColumnName().substring(0, 4) + begin;
  }

  private String generateRandomData(Field field) {
    String ColumnName = field.getColumnName();
    List<String> data = map.get(ColumnName.toLowerCase());
    if (data != null) {
      return data.get(new Random().nextInt(data.size()));
    } else {
      return ColumnName.toLowerCase();
    }
  }
}
