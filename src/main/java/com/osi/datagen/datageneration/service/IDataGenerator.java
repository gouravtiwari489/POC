package com.osi.datagen.datageneration.service;

import java.text.ParseException;
import com.osi.datagen.domain.Field;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
