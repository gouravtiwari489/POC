package com.osi.datagen.parsing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.MappingCategory;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.exception.DependencyException;

@Service
public class SQLFileReaderService {

  @Autowired private TableStructureExtractor tableStructureExtractor;

  @Autowired private FindWordMatchingPossibilities findWordMatchingPossibilities;


  public TableList readSQLfile(
      MultipartFile multiFile, String domainType, boolean dependencyCheck)
      throws DependencyException, Exception {

    File convFile = new File(multiFile.getOriginalFilename());
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multiFile.getBytes());
    List<Table> tableList =  tableStructureExtractor.searchforTableName(convFile, dependencyCheck);
    if (tableList != null && !tableList.isEmpty()) {
      for (Table entry : tableList) {
        List<Field> inputTableFields = entry.getFields();
        for (Field inputColumn : inputTableFields) {
            Map<String, List<String>> matchedWords = new LinkedHashMap<>();
             String dataType = inputColumn.getDataType();
             String inputColumnName = inputColumn.getColumnName();
            matchedWords = findWordMatchingPossibilities.findMatchingWord(inputColumnName, domainType,dataType);
            if (matchedWords != null && !matchedWords.isEmpty()) {
              Set<String> entires = matchedWords.keySet();
              List<MappingCategory> mappingCategory = new ArrayList<>();
              for (String ent : entires) {
            	  MappingCategory category = new MappingCategory();
            	  String[] splitString = ent.split("-");
            	  category.setMappingCategory(splitString[0]);
            	  category.setMappingType(splitString[1]);
            	  mappingCategory.add(category);
              }
              inputColumn.setMappingCategeries(mappingCategory);
            }
        }
      }
    }
    fos.close();
    TableList tablist=new TableList();
    tablist.setTables(tableList);
    return tablist;
  }
}
