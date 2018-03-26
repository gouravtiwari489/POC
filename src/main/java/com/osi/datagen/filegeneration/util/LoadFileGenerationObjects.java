package com.osi.datagen.filegeneration.util;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@Getter
public class LoadFileGenerationObjects {

  Map<String, GenerateDataInterface> genDataServiceMap = null;

  @PostConstruct
  public void init() {

    genDataServiceMap = new HashMap<String, GenerateDataInterface>();
    genDataServiceMap.put("csv", CsvGenerationUtil.INSTANCE);
    genDataServiceMap.put("xlsx", ExcelGenerationUtil.INSTANCE);
    genDataServiceMap.put("sql", SqlGenerationUtil.INSTANCE);
    genDataServiceMap.put("xml", XmlGenerationUtil.INSTANCE);
    genDataServiceMap.put("json", JsonGenerationUtil.INSTANCE);
  }
}
