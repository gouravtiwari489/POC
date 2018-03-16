package com.osi.datagen.datagen.intf;

import java.text.ParseException;
import com.osi.datagen.domain.Field;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
