package com.osi.datagen.service;

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
import com.osi.datagen.domain.MappedCategory;
import com.osi.datagen.domain.Table;
import com.osi.datagen.domain.TableList;
import com.osi.datagen.exception.DependencyException;
import com.osi.datagen.repository.DomainRepository;
import com.osi.datagen.utils.FindWordMatchingPossibilities;
import com.osi.datagen.utils.TableStructureExtractor;

@Service
public class SQLFileReaderService {

  @Autowired DomainRepository domainRepository;
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
              List<MappedCategory> mappedCategory = new ArrayList<>();
              for (String ent : entires) {
            	  MappedCategory category = new MappedCategory();
            	  String[] splitString = ent.split("-");
            	  category.setMappingCategory(splitString[0]);
            	  category.setMappingType(splitString[1]);
            	  mappedCategory.add(category);
              }
              inputColumn.setMappedCategory(mappedCategory);
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
