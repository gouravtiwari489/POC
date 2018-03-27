package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import com.osi.datagen.domain.Field;

public class StringDataGenerator implements IDataGenerator, IUniqueDataGenerator {
  public static Map<String, List<String>> map = new HashMap<>();
  private int begin = 1000;
  
  public StringDataGenerator(String domainType, String preferredLocale) {
    try {
      Resource resource = new ClassPathResource("datasets//" + domainType);
      resource.getFile().getPath();
      File[] files = new File(resource.getFile().getPath()).listFiles();
      for (File file : files) {
        if (!file.isDirectory()) {
          List<String> lines = FileUtils.readLines(file, "utf-8");
           map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
        }
      }
      String[] locale = preferredLocale.split("-");
      getLocaleData(locale[1], domainType); 
      }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  public StringDataGenerator(String domainType) {
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
      int character = (int) (Math.random() * DasConstants.ALPHA_NUMERIC_STRING.length());
      builder.append(DasConstants.ALPHA_NUMERIC_STRING.charAt(character));
    }
    return singleQuote(builder.toString());
  }
  
  private void getLocaleData(String preferredLocale, String domainType) {
    Resource resource =
       new ClassPathResource(DasConstants.DATASETS_PATH + domainType + "//" + preferredLocale);
    try {
      resource.getFile().getPath();

      File[] files = new File(resource.getFile().getPath()).listFiles();

     for (File file : files) {
        if (!file.isDirectory()) {
         List<String> lines = FileUtils.readLines(file, DasConstants.DATASETS_CHARACTERSET);
          map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
        }
      }
    //  getCommonData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

 /*private void getCommonData() {
    Resource resource = new ClassPathResource(DasConstants.DATASETS_PATH + DasConstants.DATASETS_COMMONS);
    try {
      resource.getFile().getPath();

      File[] files = new File(resource.getFile().getPath()).listFiles();

      for (File file : files) {
        if (!file.isDirectory()) {
          List<String> lines = FileUtils.readLines(file, DasConstants.DATASETS_CHARACTERSET);
          map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/
}
