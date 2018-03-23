package com.osi.datagen.datagenerators;
import java.util.Random;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.domain.Field;

public class LongDatagenerator implements IDataGenerator {

  int begin = 10000;

  @Override
  public String generateData(Field field) {

   
      return generateRandomData();
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

