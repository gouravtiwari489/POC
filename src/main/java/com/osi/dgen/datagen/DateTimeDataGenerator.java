package com.osi.dgen.datagen;

import static com.osi.dgen.utils.DataGenUtil.singleQuote;

import com.osi.dgen.datagen.intf.IDataGenerator;
import com.osi.dgen.domain.Field;
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
