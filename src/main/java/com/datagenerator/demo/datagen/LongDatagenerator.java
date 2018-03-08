package com.datagenerator.demo.datagen;

import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;
import java.util.Random;

public class LongDatagenerator implements IDataGenerator {

  int begin = 10000;

  @Override
  public String generateData(Field field) {

    if (field.isPrimaryKey()) {
      return generateUniqueData();
    } else {
      return generateRandomData();
    }
  }

  public String generateRandomData() {
    int begin = 1000;
    int end = 9999;
    return ((new Random().nextInt(end - begin) + 1) + begin) + "";
  }

  public String generateUniqueData() {
    begin++;
    return begin + "";
  }
}
