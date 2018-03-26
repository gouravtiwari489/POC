package com.osi.datagen.datageneration.service;

import com.osi.datagen.domain.Field;
import java.text.ParseException;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
