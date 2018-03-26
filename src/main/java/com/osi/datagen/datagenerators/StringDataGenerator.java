package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;

import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import com.osi.datagen.domain.Field;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class StringDataGenerator implements IDataGenerator, IUniqueDataGenerator {
  public static Map<String, List<String>> map = new HashMap<>();
  private int begin = 1000;
  private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz";

  public StringDataGenerator(String domainType) {
    try {
      Resource resource = new ClassPathResource("datasets//" + domainType);
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

    return generateRandomDataFromDataSets(field);
  }

  public String generateUniqueData(Field field) {
    begin++;
    return singleQuote(field.getColumnName().substring(0, 4) + begin);
  }

  private String generateRandomDataFromDataSets(Field field) {
    String columnName = field.getMappedCategory();
    List<String> data = map.get(columnName.toLowerCase());
    if (data != null) {
      return singleQuote(data.get(new Random().nextInt(data.size())));
    } else {
      return randomString(10);
    }
  }

  public String randomString(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return singleQuote(builder.toString());
  }
}
