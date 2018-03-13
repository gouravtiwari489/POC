package com.osi.dgen.datagen;

import static com.osi.dgen.utils.DataGenUtil.singleQuote;

import com.osi.dgen.datagen.intf.IDataGenerator;
import com.osi.dgen.domain.Field;
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
