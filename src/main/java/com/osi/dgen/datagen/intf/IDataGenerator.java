package com.osi.dgen.datagen.intf;

import com.osi.dgen.domain.Field;
import java.text.ParseException;

public interface IDataGenerator {

  public String generateData(Field field) throws ParseException;
}
