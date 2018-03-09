package com.datagenerator.demo.download.utils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.AbstractMap;
import java.util.Map.Entry;

public class MapEntryConverter implements Converter {
  public boolean canConvert(Class clazz) {
    return AbstractMap.class.isAssignableFrom(clazz);
  }

  @Override
  public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
    AbstractMap map = (AbstractMap) value;
    for (Object obj : map.entrySet()) {
      Entry entry = (Entry) obj;
      writer.startNode(entry.getKey().toString());
      writer.setValue(entry.getValue().toString());
      writer.endNode();
    }
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    // TODO Auto-generated method stub
    return null;
  }
}
