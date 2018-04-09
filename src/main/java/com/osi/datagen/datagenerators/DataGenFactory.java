package com.osi.datagen.datagenerators;

import com.osi.datagen.configuration.DbDataTypeConfigReader;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import java.util.HashMap;

public class DataGenFactory {
  public HashMap<String, IDataGenerator> map = new HashMap<>(8);
  public HashMap<String, IUniqueDataGenerator> UniqueGenmap = new HashMap<>(2);
  private static final String STRING = "string";
  private static final String LONG = "long";

  public DataGenFactory(String domainType, String preferredLocale) {
    createDataGenerator(domainType, preferredLocale);
    createUniqueDataGenerator(domainType);
  }

  public IDataGenerator findDataGenerator(String type) {
    return map.get(DbDataTypeConfigReader.getJavaType(type.toUpperCase()));
  }

  private void createDataGenerator(String domainType, String preferredLocale) {
    map.put(STRING, new StringDataGenerator(domainType, preferredLocale));
    map.put("double", new DoubleDataGenerator());
    map.put("bytes", new ByteDataGenerator());
    map.put(LONG, new LongDatagenerator());
    map.put("date", new DateDataGenerator());
    map.put("datetime", new DateTimeDataGenerator());
    map.put("timestamp", new TimeStampDataGenerator());
    map.put("time", new TimeDataGenerator());
  }

  public IUniqueDataGenerator findUniqueDataGenerator(String type) {
    return UniqueGenmap.get(DbDataTypeConfigReader.getJavaType(type.toUpperCase()));
  }

  private void createUniqueDataGenerator(String domainType) {
    UniqueGenmap.put(STRING, new StringDataGenerator(domainType));
    UniqueGenmap.put(LONG, new LongDatagenerator());
  }
}
