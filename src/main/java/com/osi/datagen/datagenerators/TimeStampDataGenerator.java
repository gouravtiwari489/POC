package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;

import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.domain.Field;
import java.sql.Timestamp;

public class TimeStampDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
    long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
    long diff = end - offset + 1;
    return singleQuote(new Timestamp(offset + (long) (Math.random() * diff)).toString());
  }
}
