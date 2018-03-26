package com.osi.datagen.datageneration.service;

import com.osi.datagen.domain.Field;
import java.text.ParseException;

public interface IUniqueDataGenerator {
  public String generateUniqueData(Field field) throws ParseException;
}
