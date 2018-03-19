package com.osi.datagen.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.osi.datagen.domain.CheckConstraint;
import com.osi.datagen.domain.Constraint;
import com.osi.datagen.domain.Field;
import com.osi.datagen.domain.ForigenKeyConstraint;
import com.osi.datagen.domain.Table;
import com.osi.datagen.exception.DependencyException;

@Component
public class TableStructureExtractor {

  @Value("${dependencycheck.toggle}")
  String toggleCheck;

  private static final String ENGINE = "ENGINE";
  private static final String REFERENCES = "REFERENCES ";
  private static final String FOREIGN_KEY = "FOREIGN KEY ";
  private static final String PRIMARY_KEY = "PRIMARY KEY ";
  private static final String CREATE_TABLE = "CREATE TABLE ";
  private static final String CONSTRAINT = "CONSTRAINT ";
  private static final String NOT_NULL = "NOT NULL";
  private static final String CHECK = "CHECK";
  private static final String KEY = "KEY";
  public List<Table> searchforTableName(
      File file, boolean dependencyCheck) throws DependencyException, Exception {
    List<Field> fieldsList = new  ArrayList<>();
	List<Constraint> constraintList= new  ArrayList<>();
	List<ForigenKeyConstraint> forigenKeysList = new  ArrayList<>();
	List<CheckConstraint> checkConstraintsList =new  ArrayList<>();
	List<Table> tableList = new ArrayList<>();
    Table table = null;
    final Scanner scanner = new Scanner(file);
    String tableName = "", primaryKey = "";
    int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile != null && !lineFromFile.isEmpty() && !lineFromFile.startsWith("/*")
					&& !lineFromFile.startsWith("--")) {
				if (lineFromFile.contains(CREATE_TABLE)) {
					table = new Table();
					tableName = "";
					fkCount = 1;
					String[] matchString = lineFromFile.split(CREATE_TABLE);
					tableName = matchString[1].split(" ")[0].replace("`", "");
					count = 1;
					table.setTableName(tableName);
				} else if (lineFromFile.contains(PRIMARY_KEY)) {
					List<String> pkColumList = new ArrayList<>();
					Constraint constraint = new Constraint();
					constraint.setConstraintType(PRIMARY_KEY);
					primaryKey = "";
					count = 0;
					String[] pkString = lineFromFile.split(PRIMARY_KEY);
					primaryKey = pkString[1].split(" ")[0].replace("`", "");
					String[] pkString2 = null;
					if (primaryKey.endsWith(",")) {
						pkString2 = primaryKey.split(",");
						if (pkString2.length > 1) {
							primaryKey = pkString2[0] + "," + pkString2[1];
							pkColumList.add(pkString2[0].replaceAll("[^\\,a-zA-Z0-9]+", ""));
							pkColumList.add(pkString2[1].replaceAll("[^\\,a-zA-Z0-9]+", ""));
						}
						else {
							primaryKey = pkString2[0];
							pkColumList.add(pkString2[0].replaceAll("[^\\,a-zA-Z0-9]+", ""));
						}
					}
					constraint.setColumns(pkColumList);
					constraintList.add(constraint);
				} else if (lineFromFile.contains(CONSTRAINT)) {
					if(lineFromFile.contains(FOREIGN_KEY)) {
						ForigenKeyConstraint fkConstraint = new ForigenKeyConstraint();
						String[] fieldString = lineFromFile.split(FOREIGN_KEY);
						fkConstraint.setConstraintName(fieldString[0].replace(CONSTRAINT, "").replaceAll("`", ""));
						String[] fieldString2 = fieldString[1].split(REFERENCES);
						System.out.println("fieldString for fk is::" + fieldString2[0] + "--" + fieldString2[1]);
						String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
						String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "")
								.replace("ONUPDATECASCADE", "");
						count = 0;
						fkCount++;
						fkConstraint.setKeyName(test1);
						String[] test3 = test2.split("\\(");
						fkConstraint.setReferenceTable(test3[0]);
						fkConstraint.setReferenceColumn(test3[1].replace("\\)", ""));
						forigenKeysList.add(fkConstraint);
					}else if(lineFromFile.contains(CHECK)) {
						CheckConstraint checkConstraint = new CheckConstraint();
						String[] chkConstraintSplit = lineFromFile.split(CHECK);
						checkConstraint.setConstraintName(chkConstraintSplit[0].replace(CONSTRAINT, "").replaceAll("`", "").trim());
						checkConstraint.setValue(chkConstraintSplit[1].replaceAll("[^\\>= ,a-zA-Z0-9]+", "").trim());
						checkConstraintsList.add(checkConstraint);
					}
				}else if(lineFromFile.contains(CHECK)) {
					CheckConstraint checkConstraint = new CheckConstraint();
					String[] chkConstraintSplit = lineFromFile.split(CHECK);
					checkConstraint.setConstraintName(CHECK);
					checkConstraint.setValue(chkConstraintSplit[0]);
					checkConstraintsList.add(checkConstraint);
				} else if (lineFromFile.contains(ENGINE)) {
					count = 0;
					table.setFields(fieldsList);
					table.setCheckConstraints(checkConstraintsList);
					table.setConstraints(constraintList);
					table.setForigenKeys(forigenKeysList);
					tableList.add(table);
					table = new Table();
					fieldsList=new ArrayList<>();
					constraintList=new ArrayList<>();
					forigenKeysList=new ArrayList<>();
					checkConstraintsList=new ArrayList<>();
				} else if (count == 1) {
					List<String> pkColumList = new ArrayList<>();
					Constraint constraint = new Constraint();
					Field coulmnField = new Field();
					String lineFromFile1 = lineFromFile.trim().replaceAll("[^\\(),a-zA-Z0-9]+", " ").trim();
					String[] splitString =  lineFromFile1.split(" ");
					coulmnField.setColumnName(splitString[0]);
					coulmnField.setDataType(splitString[1]);
					if(splitString.length>2) {
						coulmnField.setDefaultValue(splitString[2]+" "+splitString[3]);
						constraint.setConstraintType(splitString[2]+" "+splitString[3]);
						pkColumList.add(splitString[0]);
					}
					constraint.setColumns(pkColumList);
					constraintList.add(constraint);
					fieldsList.add(coulmnField);
					String[] fieldString = lineFromFile1.split(" ");
					if (fieldString.length >= 2) {
						String field = fieldString[0];
						String fieldType = fieldString[1];
						String[] fieldType2 = null;
						if (fieldType.endsWith(",")) {
							fieldType2 = fieldType.split(",");
							fieldType = fieldType2[0];
						}
					}
				}

			}
		}
    scanner.close();
    return tableList;
  }

  private void reOrderTableStructure(
      LinkedHashMap<String, LinkedHashMap<String, String>> tableMap, boolean dependencyCheck)
      throws DependencyException, Exception {
    LinkedHashMap<String, List<String>> fkListMap = createFKListMap(tableMap);
    String msg = "";
    String message = "";
    boolean dependCheck = Boolean.parseBoolean(toggleCheck);
    for (Map.Entry<String, List<String>> entry : fkListMap.entrySet()) {
      String parentTbl = entry.getKey();
      List<String> childTbls = entry.getValue();
      if (childTbls.contains(parentTbl)) msg = "Table " + parentTbl + " has self join";
      else {
        for (String chldtbl : childTbls) {
          List<String> childTbls2 = fkListMap.get(chldtbl);
          if (childTbls2.contains(parentTbl)) {
            if (!msg.isEmpty())
              msg = msg + " and   " + parentTbl + " has cyclic dependency with " + chldtbl;
            else msg = parentTbl + " has cyclic dependency with " + chldtbl;
          }
        }
      }
    }

    if (!msg.isEmpty()) {
      message = dependCheck ? message = "Error!" + msg : "Warning!" + msg;
      if (dependCheck) {
        throw new DependencyException(message);
      } else if (!dependCheck && dependencyCheck) {
        throw new DependencyException(message);
      } else if (!dependCheck && !dependencyCheck) {
        System.out.println("dependency ignored and proceed");
      }
    }

   }

  private LinkedHashMap<String, List<String>> createFKListMap(
      LinkedHashMap<String, LinkedHashMap<String, String>> tableMap) {
    LinkedHashMap<String, List<String>> fkListMap = new LinkedHashMap<>();
    for (Map.Entry<String, LinkedHashMap<String, String>> entry : tableMap.entrySet()) {
      String tableName2 = entry.getKey();
      LinkedHashMap<String, String> tableFields = entry.getValue();
      List<String> list = new LinkedList<String>();
      for (String inputColumnName : tableFields.keySet()) {
        System.out.println("inputColumnName is ---" + inputColumnName);
        if (inputColumnName.startsWith("FK")) {
          String[] fieldString = inputColumnName.split("->");
          String field = fieldString[1];
          String association = tableFields.get(inputColumnName);
          System.out.println("tblName is ---" + field);
          System.out.println("association is ---" + association);
          String[] associationSplit = association.split("\\(");
          String tblName = associationSplit[0];
          String colName = associationSplit[1].replace(")", "");
          System.out.println("tblName is ---" + tblName + "---colName--" + colName);

          list.add(tblName);
        }
      }
      fkListMap.put(tableName2, list);
    }
    return fkListMap;
  }

  public Map<Integer, List<String>> transform(LinkedHashMap<String, List<String>> tableMap) {
    Map<Integer, List<String>> newMap = new LinkedHashMap<Integer, List<String>>();
    for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
      transformMaptoListData_1(newMap, entry.getKey(), entry.getValue(), tableMap);
    }
    return newMap;
  }

  private void transformMaptoListData_1(
      Map<Integer, List<String>> orderedMap,
      String child,
      List<String> list,
      LinkedHashMap<String, List<String>> tableMap) {
    for (String parentTable : list) {
      Integer parentOrderLevel = 0;
      Integer childOrderLevel = 0;
      for (Map.Entry<Integer, List<String>> entry : orderedMap.entrySet()) {
        if (entry.getValue().contains(parentTable) && parentOrderLevel < entry.getKey()) {
          parentOrderLevel = entry.getKey();
        }
        if (entry.getValue().contains(child)) {
          childOrderLevel = entry.getKey();
        }
      }
      if (parentOrderLevel == 0) {
        parentOrderLevel = addChildIntoMap(parentTable, orderedMap, parentOrderLevel);
      }
      if (childOrderLevel > 0) {
        if (parentOrderLevel >= childOrderLevel) // if parent order
        // number is greater
        // than child then put
        // child below parent or
        // leave as it is in
        // order
        {
          List<String> childDependentTables =null;
          if (isTablesOnCyclicDependenctysOnDiffPrioriTy(parentTable, child, tableMap))
          {
              if(parentOrderLevel > childOrderLevel)
              {
                orderedMap.get(childOrderLevel).remove(child);
                addChildIntoMap(child, orderedMap, parentOrderLevel-1);
                addChildIntoMap(parentTable+"-->>"+child, orderedMap, parentOrderLevel-1);
                childDependentTables = findDependencies(child, child, tableMap);
                childDependentTables.remove(parentTable);
                findChildDependencies(child, orderedMap, parentOrderLevel, tableMap,childDependentTables);
              }
           }
          else
          {
            orderedMap.get(childOrderLevel).remove(child);
            parentOrderLevel = addChildIntoMap(child, orderedMap, parentOrderLevel);
            childDependentTables = findDependencies(child, child, tableMap);
            findChildDependencies(child, orderedMap, parentOrderLevel, tableMap,childDependentTables);
          }
          
        }
      } else {
        addChildIntoMap(child, orderedMap, parentOrderLevel);
      }
    }
    if (list.size() == 0) {
      addChildIntoMap(child, orderedMap, 0);
    }
  }

  private boolean isTablesOnCyclicDependenctysOnDiffPrioriTy(
      String parentTable,
      String child,
      LinkedHashMap<String, List<String>> tableMap) {
    boolean isTrue = false;
     for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
        if (entry.getKey().equals(child)) {
          break;
        }
        if (entry.getValue().contains(child) && entry.getKey().equals(parentTable)) {
          isTrue = true;
        }
      }
    return isTrue;
  }

  private int addChildIntoMap(
      String child, Map<Integer, List<String>> newMap, Integer parentOrderLevel) {

    int newParentLevel = parentOrderLevel.intValue() + 1;
    List<String> OrderLevelist = newMap.get(newParentLevel);
    if (!(OrderLevelist != null && OrderLevelist.size() > 0)) {
      OrderLevelist = new ArrayList<String>();
      newMap.put(parentOrderLevel + 1, OrderLevelist);
    }
    newMap.get(newParentLevel).add(child);
    return newParentLevel;
  }

  private void findChildDependencies(
      String child,
      Map<Integer, List<String>> orderedMap,
      Integer parentOrderLevel,
      LinkedHashMap<String, List<String>> tableMap, List<String> childDependentTables) {
  
    for (String childTable : childDependentTables) {
      ListIterator<Integer> litt = new LinkedList<Integer>(orderedMap.keySet()).listIterator();
      while (litt.hasNext()) {
        Integer entry = litt.next();
        if (orderedMap.get(entry).contains(childTable)) {
          if (entry <= parentOrderLevel) {
            List<String> parentDependentTables = findDependencies(childTable, child, tableMap);
            List<String> tables = orderedMap.get(entry);
            tables.remove(childTable);
            orderedMap.put(entry, tables);
            if (parentDependentTables.contains(child)) {
                addChildIntoMap(childTable, orderedMap, parentOrderLevel - 1);
                childTable = childTable + "-->>" + child;
                addChildIntoMap(childTable, orderedMap, parentOrderLevel - 1); 
                continue;
            }
            addChildIntoMap(childTable, orderedMap, parentOrderLevel);
          }
        }
      }
    }
  }

  private List<String> findDependencies(
      String parent, String child, LinkedHashMap<String, List<String>> tableMap) {
    List<String> dependentTables = new ArrayList<String>();
    for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
      if (entry.getValue().contains(parent)) {
        dependentTables.add(entry.getKey());
      }
      if (entry.getKey().equals(child)) {
        break;
      }
    }
    return dependentTables;
  }
  
  
}
