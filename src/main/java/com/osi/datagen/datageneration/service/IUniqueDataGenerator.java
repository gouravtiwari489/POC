package com.osi.datagen.datageneration.service;

import java.text.ParseException;
import com.osi.datagen.domain.Field;

public interface IUniqueDataGenerator {
  public String generateUniqueData(Field field) throws ParseException;

}
