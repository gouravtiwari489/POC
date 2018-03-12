package com.datagenerator.das.datagen;

import static com.datagenerator.das.utils.DataGenUtil.singleQuote;

import com.datagenerator.das.datagen.intf.IDataGenerator;
import com.datagenerator.das.domain.Field;
import java.text.ParseException;
import java.util.Random;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    byte[] b = new byte[20];
    new Random().nextBytes(b);
    return singleQuote((new String(b)));
  }
}
