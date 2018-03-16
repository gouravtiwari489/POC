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

  public List<LinkedHashMap<String, LinkedHashMap<String, String>>> readSQLfile(
      MultipartFile multiFile, String domainType, boolean dependencyCheck)
      throws DependencyException, Exception {

	List<Table> tables = new ArrayList<>();
    File convFile = new File(multiFile.getOriginalFilename());
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multiFile.getBytes());
    LinkedHashMap<String, LinkedHashMap<String, String>> inputTableMap =
        tableStructureExtractor.searchforTableName(convFile, dependencyCheck);
    LinkedHashMap<String, LinkedHashMap<String, String>> finalInputMap = new LinkedHashMap<>();
    LinkedHashMap<String, LinkedHashMap<String, String>> finalMappedMap = new LinkedHashMap<>();
    Map<String, LinkedHashMap<String, String>> availableTables = new LinkedHashMap<>();
    if (inputTableMap != null && !inputTableMap.isEmpty()) {
      for (Map.Entry<String, LinkedHashMap<String, String>> entry : inputTableMap.entrySet()) {
        String inputTableName = entry.getKey();
        LinkedHashMap<String, String> inputTableFields = entry.getValue();
        LinkedHashMap<String, String> columnCatMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> mappedMap = new LinkedHashMap<>();
        for (String inputColumnName : inputTableFields.keySet()) {
          String cat = "";
        //  if (!inputColumnName.startsWith("PK") && !inputColumnName.startsWith("FK")) {
            Map<String, List<String>> matchedWords = new LinkedHashMap<>();
             String dataType = inputTableFields.get(inputColumnName);
            matchedWords =
                findWordMatchingPossibilities.findMatchingWord(inputColumnName, domainType,dataType);
            if (matchedWords != null && !matchedWords.isEmpty()) {
              Set<String> entires = matchedWords.keySet();
              for (String ent : entires) {
                if (cat == null || cat.isEmpty()) cat = ent;
                else cat = cat + " " + ent;
              }
            }
            columnCatMap.put(inputColumnName, cat);
            mappedMap.put(inputColumnName + "<>" + cat, inputTableFields.get(inputColumnName));
         // }
        }
        availableTables.put(inputTableName, columnCatMap);
        finalMappedMap.put(inputTableName, mappedMap);
      }
    }
    fos.close();
    for (Map.Entry<String, LinkedHashMap<String, String>> entry : inputTableMap.entrySet()) {
      LinkedHashMap<String, String> inputTableFields = entry.getValue();
      LinkedHashMap<String, String> finalInputTableFields = new LinkedHashMap<>();
      for (String inputColumnName : inputTableFields.keySet()) {
        if (!inputColumnName.startsWith("PK") && !inputColumnName.startsWith("FK"))
          finalInputTableFields.put(inputColumnName, inputTableFields.get(inputColumnName));
      }
      finalInputMap.put(entry.getKey(), finalInputTableFields);
    }
    List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = new ArrayList<>();
    List<LinkedHashMap<String, LinkedHashMap<String, String>>> list2 = new ArrayList<>();
    list.add(inputTableMap);
    list.add(finalMappedMap);
    list2.add(finalInputMap);
    list2.add(finalMappedMap);
    //    customTokenConverter.setAdditionalInfo("mappedTables", list);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    user.setMappedTables(list);
    
    tables = getTableData(finalMappedMap);
    return list2;
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
						field.setMappingCategory(columnCatSplit[0]);
						field.setMappingType(columnCatSplit[1]);
					}
					field.setColumnName(columnFld[0]);
					field.setDataType(fieldEntry.getValue());
					if (fields.get("PK<>").contains(field.getColumnName()))
						field.setPrimaryKey(true);
					//if (fields.keySet().contains(field.getColumnName())) {
					if (fieldEntry.getKey().startsWith("FK")) {
						String fkKey = fieldEntry.getKey();
						String[] fkKeySplit = fkKey.split("->");
					//	String[] fkKeySplit2 = 
						String referenceValues = fieldEntry.getValue();
						String[] referenceValuesSplit = referenceValues.split("\\(");
						for(Field fld:columns ) {
							if(fkKeySplit[1].contains(fld.getColumnName())) {
								fld.setForeignKey(true);
								fld.setReferenceTable(referenceValuesSplit[0]);
								fld.setReferenceColumn(referenceValuesSplit[1].replace(")", ""));
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
