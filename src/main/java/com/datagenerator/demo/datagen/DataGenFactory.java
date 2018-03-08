package com.datagenerator.demo.datagen;

import com.datagenerator.demo.configuration.DBDataTypeConfigReader;
import com.datagenerator.demo.datagen.intf.IDataGenerator;
import java.util.HashMap;

public class DataGenFactory {
  public static HashMap<String, IDataGenerator> map = new HashMap<>();

  public static IDataGenerator createDataGenerator(String type) {
    IDataGenerator dataGen = null;
    if (map.isEmpty()) {
      map.put("string", new StringDataGenerator());
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
