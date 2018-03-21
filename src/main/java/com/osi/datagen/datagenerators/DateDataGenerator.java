package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;
import java.sql.Timestamp;
import java.util.Date;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.domain.Field;

public class DateDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    long beginTime = Timestamp.valueOf("1900-01-01 00:00:00").getTime();
    long endTime = System.currentTimeMillis();
    long diff = endTime - beginTime + 1;
    return singleQuote(new Date(beginTime + (long) (Math.random() * diff)).toString());
  }
}
