package com.datagenerator.das.datagen;

import static com.datagenerator.das.utils.DataGenUtil.singleQuote;

import com.datagenerator.das.datagen.intf.IDataGenerator;
import com.datagenerator.das.domain.Field;
import java.sql.Timestamp;

public class DateTimeDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
    long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
    long diff = end - offset + 1;
    return singleQuote(new Timestamp(offset + (long) (Math.random() * diff)).toString());
  }
}
