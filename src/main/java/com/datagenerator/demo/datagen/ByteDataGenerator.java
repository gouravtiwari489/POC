package com.datagenerator.demo.datagen;

import java.text.ParseException;
import java.util.Random;
import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    byte[] b = new byte[20];
    new Random().nextBytes(b);
    return  new String(b);
  }
}
