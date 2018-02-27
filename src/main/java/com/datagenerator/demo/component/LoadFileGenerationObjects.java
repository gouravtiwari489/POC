package com.datagenerator.demo.component;

import com.datagenerator.demo.download.utils.CSVGenerationUtil;
import com.datagenerator.demo.download.utils.ExcelGenerationUtil;
import com.datagenerator.demo.download.utils.GenerateDataInterface;
import com.datagenerator.demo.download.utils.SQLGenerationUtil;
import com.datagenerator.demo.download.utils.XMLGenerationUtil;
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
    genDataServiceMap.put("csv", CSVGenerationUtil.INSTANCE);
    genDataServiceMap.put("xlsx", ExcelGenerationUtil.INSTANCE);
    genDataServiceMap.put("sql", SQLGenerationUtil.INSTANCE);
    genDataServiceMap.put("xml", XMLGenerationUtil.INSTANCE);
  }
}
