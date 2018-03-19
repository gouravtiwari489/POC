/*package com.osi.datagen.datagen;

import static com.osi.datagen.utils.DataGenUtil.singleQuote;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.osi.datagen.datagen.intf.IDataGenerator;
import com.osi.datagen.domain.Field;

public class StringDataGenerator implements IDataGenerator {
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
    /*if (field.isPrimaryKey()) {
      return generateUniqueDatawithNum(field);

    } else{
      return generateRandomDataFromDataSets(field);
    }
  }

  private String generateUniqueDatawithNum(Field field) {
    begin++;
    return singleQuote(field.getColumnName().substring(0, 4) + begin);
  }

  private String generateRandomDataFromDataSets(Field field) {
    String ColumnName = field.getColumnName();
    List<String> data = map.get(ColumnName.toLowerCase());
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
*/
