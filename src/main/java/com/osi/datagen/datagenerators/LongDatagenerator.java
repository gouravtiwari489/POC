package com.osi.datagen.datagenerators;

import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import com.osi.datagen.domain.Field;
import java.text.ParseException;
import java.util.Random;

public class LongDatagenerator implements IDataGenerator, IUniqueDataGenerator {

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

  @Override
  public String generateUniqueData(Field field) throws ParseException {
    begin++;
    return begin + "";
  }
}
