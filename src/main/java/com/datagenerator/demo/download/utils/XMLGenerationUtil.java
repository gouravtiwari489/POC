package com.datagenerator.demo.download.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.thoughtworks.xstream.XStream;

public enum XMLGenerationUtil implements GenerateDataInterface {
  INSTANCE;

  @Override
  public void generateData(
      String tableName, List<List<String>> excelData, String fileType, CustomUserDetails user) {
    
	  Map<String, String> fieldValueMap = null;
	  String dataXml = new String("<root>\n");
	  XStream xstream = new XStream();
	  xstream.alias("row", LinkedHashMap.class);
	  xstream.registerConverter(new MapEntryConverter());
	  List<String> fields = excelData.get(0);
	  for (int i = 1; i < excelData.size(); i++) {
		  fieldValueMap = new LinkedHashMap<>();
		  List<String> values = excelData.get(i);
		  for (int j = 0; j < values.size(); j++) {
			  fieldValueMap.put(fields.get(j), values.get(j));
		  }
		  dataXml += xstream.toXML(fieldValueMap) +  "\n";
	  }
	  dataXml += "</root>";
	  try {
		  writeToFile(dataXml, tableName, fileType, user);
	  } catch (IOException e) {
		  e.getMessage();
	  }
  }

  @Override
  public void writeToFile(Object obj, String tableName, String fileType, CustomUserDetails user)
      throws IOException, FileNotFoundException {
	  	String filePath;
	    Resource resource = new ClassPathResource("output\\" + user.getUsername());
	    if (!resource.exists()) {
	      new File("bin\\output\\" + user.getUsername()).mkdir();
	    }
	    File file = new File(resource.getFile().getPath() + "/" + fileType);
	    if (!file.exists()) {
	      file.mkdir();
	    }
	    filePath =
	        String.format(
	            "%s\\%s.%s", resource.getFile().getPath() + "\\" + fileType, tableName, fileType);
	    BufferedWriter jsonFile = new BufferedWriter(new FileWriter(filePath));
	    jsonFile.write((String) obj);
	    jsonFile.flush();
	    jsonFile.close();
  }
}
