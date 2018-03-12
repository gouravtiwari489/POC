package com.datagenerator.das.datagen;

import static com.datagenerator.das.utils.DataGenUtil.singleQuote;

import com.datagenerator.das.datagen.intf.IDataGenerator;
import com.datagenerator.das.domain.Field;
import java.sql.Timestamp;
import java.util.Date;

public class DateDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) {
    long beginTime = Timestamp.valueOf("1900-01-01 00:00:00").getTime();
    long endTime = System.currentTimeMillis();
    long diff = endTime - beginTime + 1;
    return singleQuote(new Date(beginTime + (long) (Math.random() * diff)).toString());
  }
}
