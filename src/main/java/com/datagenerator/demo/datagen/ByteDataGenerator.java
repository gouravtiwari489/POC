package com.datagenerator.demo.datagen;

import com.datagenerator.demo.datagen.intf.IDataGenerator;
import com.datagenerator.demo.domain.Field;
import java.text.ParseException;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    // TODO Auto-generated method stub
    return "byte";
  }
}
