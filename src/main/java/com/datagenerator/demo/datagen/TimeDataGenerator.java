package com.datagenerator.demo.datagen;

import static com.datagenerator.demo.utils.DataGenUtil.singleQuote;

import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;
import java.sql.Time;
import java.util.Random;

public class TimeDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    Random random = new Random();
    int millisInDay = 24 * 60 * 60 * 1000;
    return singleQuote(new Time((long) random.nextInt(millisInDay)).toString());
  }
}
