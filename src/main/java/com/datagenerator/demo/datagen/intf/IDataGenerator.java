package com.datagenerator.demo.datagen.intf;

import com.datagenerator.demo.domain.Field;
import java.text.ParseException;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
