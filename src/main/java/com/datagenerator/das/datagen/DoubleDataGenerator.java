package com.datagenerator.das.datagen;

import com.datagenerator.das.datagen.intf.IDataGenerator;
import com.datagenerator.das.domain.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;

public class DoubleDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    double begin = 1000d;
    double end = 9999d;
    DecimalFormat df = new DecimalFormat("0.00");
    String number = df.format(begin + new Random().nextDouble() * (end - begin));
    return df.parse(number).toString();
  }
}
