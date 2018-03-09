package com.datagenerator.demo.datagen;

import static com.datagenerator.demo.utils.DataGenUtil.singleQuote;

import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;
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
