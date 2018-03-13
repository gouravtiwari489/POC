package com.osi.dgen.datagen;

import com.osi.dgen.configuration.DBDataTypeConfigReader;
import com.osi.dgen.datagen.intf.IDataGenerator;
import java.util.HashMap;

public class DataGenFactory {
  public static HashMap<String, IDataGenerator> map = new HashMap<>();

  public static IDataGenerator createDataGenerator(String type, String domainType) {
    IDataGenerator dataGen = null;
    if (map.isEmpty()) {
      map.put("string", new StringDataGenerator(domainType));
      map.put("double", new DoubleDataGenerator());
      map.put("bytes", new ByteDataGenerator());
      map.put("long", new LongDatagenerator());
      map.put("date", new DateDataGenerator());
      map.put("datetime", new DateTimeDataGenerator());
      map.put("timestamp", new TimeStampDataGenerator());
      map.put("time", new TimeDataGenerator());
    }
    dataGen = map.get(DBDataTypeConfigReader.getJavaType(type.toUpperCase()));

    return dataGen;
  }
}
