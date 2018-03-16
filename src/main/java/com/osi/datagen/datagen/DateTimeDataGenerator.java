package com.osi.datagen.datagen;

import static com.osi.datagen.utils.DataGenUtil.singleQuote;
import java.sql.Timestamp;
import com.osi.datagen.datagen.intf.IDataGenerator;
import com.osi.datagen.domain.Field;

public class DateTimeDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    long offset = Timestamp.valueOf("2012-01-01 00:00:00").getTime();
    long end = Timestamp.valueOf("2013-01-01 00:00:00").getTime();
    long diff = end - offset + 1;
    return singleQuote(new Timestamp(offset + (long) (Math.random() * diff)).toString());
  }
}
