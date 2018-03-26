package com.osi.datagen.datagenerators;

import com.osi.datagen.configuration.DbDataTypeConfigReader;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import java.util.HashMap;

public class DataGenFactory {
  public static HashMap<String, IDataGenerator> map = new HashMap<>();
  public static HashMap<String, IUniqueDataGenerator> UniqueGenmap = new HashMap<>();

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
    dataGen = map.get(DbDataTypeConfigReader.getJavaType(type.toUpperCase()));

    return dataGen;
  }

  public static IUniqueDataGenerator createUniqueDataGenerator(String type, String domainType) {
    IUniqueDataGenerator dataGen = null;
    if (UniqueGenmap.isEmpty()) {
      UniqueGenmap.put("string", new StringDataGenerator(domainType));
      UniqueGenmap.put("long", new LongDatagenerator());
    }
    dataGen = UniqueGenmap.get(DbDataTypeConfigReader.getJavaType(type.toUpperCase()));

    return dataGen;
  }
}
