package com.osi.datagen.component;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.osi.datagen.download.utils.CSVGenerationUtil;
import com.osi.datagen.download.utils.ExcelGenerationUtil;
import com.osi.datagen.download.utils.GenerateDataInterface;
import com.osi.datagen.download.utils.JSONGenerationUtil;
import com.osi.datagen.download.utils.SQLGenerationUtil;
import com.osi.datagen.download.utils.XMLGenerationUtil;

@Component
@Scope("singleton")
@Getter
public class LoadFileGenerationObjects {

  Map<String, GenerateDataInterface> genDataServiceMap = null;

  @PostConstruct
  public void init() {

    genDataServiceMap = new HashMap<String, GenerateDataInterface>();
    genDataServiceMap.put("csv", CSVGenerationUtil.INSTANCE);
    genDataServiceMap.put("xlsx", ExcelGenerationUtil.INSTANCE);
    genDataServiceMap.put("sql", SQLGenerationUtil.INSTANCE);
    genDataServiceMap.put("xml", XMLGenerationUtil.INSTANCE);
    genDataServiceMap.put("json", JSONGenerationUtil.INSTANCE);
  }
}
