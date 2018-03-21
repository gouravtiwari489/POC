package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;
import java.sql.Time;
import java.util.Random;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.domain.Field;

public class TimeDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    Random random = new Random();
    int millisInDay = 24 * 60 * 60 * 1000;
    return singleQuote(new Time((long) random.nextInt(millisInDay)).toString());
  }
}
