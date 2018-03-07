package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Dataset {
  public static Map<String, List<String>> map = new HashMap<>();
  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final Random rng = new SecureRandom();

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

  public static String getRandomData(String ColumnName, String maxLength) {
    List<String> data = map.get(ColumnName.toLowerCase());
    String randomString = "";
    if (data != null) {
      String resultData = data.get(new Random().nextInt(data.size()));
      if(resultData.length() < Integer.parseInt(maxLength)) 
    	  randomString = resultData;
    } else {
    	randomString = ColumnName.toLowerCase();
    }
	return randomString;
  }

  public static Date getRandomDate() {

    long beginTime = Timestamp.valueOf("1900-01-01 00:00:00").getTime();
    long endTime = System.currentTimeMillis();
    long diff = endTime - beginTime + 1;
    return new Date(beginTime + (long) (Math.random() * diff));
  }

  public static String getRandomDecimal() throws ParseException {

    double begin = 1000d;
    double end = 9999d;
    DecimalFormat df = new DecimalFormat("0.00");
    String number = df.format(begin + new Random().nextDouble() * (end - begin));
    return df.parse(number).toString();
  }

  public static String getRandomInt(String maxLength) {
    int begin = 1000;
    int end = 9999;
    String randomResult = "";
    String resultData = ((new Random().nextInt(end - begin) + 1) + begin) + "";
    if(resultData.length() < Integer.parseInt(maxLength)) 
    	randomResult = resultData;
    
    return randomResult;
  }

}
