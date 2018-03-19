package com.osi.datagen.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.MappedCategory;
import com.osi.datagen.domain.Table;
import com.osi.datagen.exception.DependencyException;
import com.osi.datagen.repository.DomainRepository;
import com.osi.datagen.utils.FindWordMatchingPossibilities;
import com.osi.datagen.utils.TableStructureExtractor;

@Service
public class SQLFileReaderService {

  @Autowired DomainRepository domainRepository;
  @Autowired private TableStructureExtractor tableStructureExtractor;

  @Autowired private FindWordMatchingPossibilities findWordMatchingPossibilities;

  //  @Autowired private CustomTokenConverter customTokenConverter;

  public List<Table> readSQLfile(
      MultipartFile multiFile, String domainType, boolean dependencyCheck)
      throws DependencyException, Exception {

	List<Table> tables = new ArrayList<>();
    File convFile = new File(multiFile.getOriginalFilename());
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multiFile.getBytes());
    List<Table> tableList =  tableStructureExtractor.searchforTableName(convFile, dependencyCheck);
    if (tableList != null && !tableList.isEmpty()) {
      for (Table entry : tableList) {
        List<Field> inputTableFields = entry.getFields();
        for (Field inputColumn : inputTableFields) {
        	String cat = "";
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
    //    customTokenConverter.setAdditionalInfo("mappedTables", list);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
  //  user.setMappedTables(tables);
    return tableList;
    // Unmapped entity needs to be saved?
  }
  
private List<Table> getTableData(LinkedHashMap<String, LinkedHashMap<String, String>> tableMap){
	  
	  List<Table> tables = new ArrayList<>();
	  
	  for (Map.Entry<String, LinkedHashMap<String, String>> entry : tableMap.entrySet()) {
		  Table table = new Table();
		  table.setTableName(entry.getKey());
		  List<Field> columns = new ArrayList<>();
		  LinkedHashMap<String, String> fields = entry.getValue();
			for (Map.Entry<String, String> fieldEntry : fields.entrySet()) {
					Field field = new Field();
					boolean flag = false;
					String[] columnFld = fieldEntry.getKey().split("<>");
					if(columnFld.length >1 && columnFld[1]!=null && !columnFld[1].isEmpty()) {
						String[] columnCatSplit = columnFld[1].split("-");
				//		field.setMappingCategory(columnCatSplit[0]);
				//		field.setMappingType(columnCatSplit[1]);
					}
					field.setColumnName(columnFld[0]);
					field.setDataType(fieldEntry.getValue());
					//if (fields.get("PK<>").contains(field.getColumnName()))
					//	field.setPrimaryKey(true);
					//if (fields.keySet().contains(field.getColumnName())) {
					if (fieldEntry.getKey().startsWith("FK")) {
						String fkKey = fieldEntry.getKey();
						String[] fkKeySplit = fkKey.split("->");
					//	String[] fkKeySplit2 = 
						String referenceValues = fieldEntry.getValue();
						String[] referenceValuesSplit = referenceValues.split("\\(");
						for(Field fld:columns ) {
							if(fkKeySplit[1].contains(fld.getColumnName())) {
								//fld.setForeignKey(true);
								//fld.setReferenceTable(referenceValuesSplit[0]);
								//fld.setReferenceColumn(referenceValuesSplit[1].replace(")", ""));
							}
						}
						flag = true;
						
					}
					if(!flag && !field.getColumnName().startsWith("PK"))
					 columns.add(field);
			}
		  table.setFields(columns);
		  tables.add(table);
	  }
	  return tables;
  }
  
}
