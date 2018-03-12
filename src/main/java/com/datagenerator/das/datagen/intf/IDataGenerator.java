package com.datagenerator.das.datagen.intf;

import com.datagenerator.das.domain.Field;
import java.text.ParseException;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
