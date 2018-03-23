package com.osi.datagen.parsing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
  private static final String REFERENCES = "REFERENCES";
  private static final String FOREIGN_KEY = "FOREIGN KEY";
  private static final String PRIMARY_KEY = "PRIMARY KEY";
  private static final String CREATE_TABLE = "CREATE TABLE ";
  private static final String CONSTRAINT = "CONSTRAINT";
  private static final String CHECK = "CHECK";
  private static final String UNIQUE_KEY = "UNIQUE KEY";
  
  private static final String FIELDEXP =  "[^\\()_,a-zA-Z0-9]+";
  private static final String CHECKEXP = "[^\\>=_ ,a-zA-Z0-9]+";
  private static final String PRIFOREIGNEXP = "[^\\_,a-zA-Z0-9]+";
  private static final String COMMENGTEXP = "(--.*)|(((/\\*)+?[\\w\\W]+?(\\*/)+))";
  
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
    String multiLineUncommented="";
    boolean isMultiLine=false;
    int count = 0;
    int fkCount = 1;
		while (scanner.hasNextLine()) {
			final String readLine = scanner.nextLine().trim();
			String lineFromFile = readLine.replaceAll(COMMENGTEXP,""); //remove Single line comment
			//remove Multiline comments
			if(lineFromFile.indexOf("/*")>-1 )
			{
				multiLineUncommented=lineFromFile.substring(0, lineFromFile.indexOf("/*"));
				isMultiLine=true;
			}
			if(isMultiLine && !(lineFromFile.indexOf("*/")>-1))
			{
				continue;
			}
			else if(isMultiLine)
			{
				isMultiLine=false;
				lineFromFile=multiLineUncommented;
			}
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
					primaryKey = pkString[1].replaceAll("[^\\,_,a-zA-Z0-9]+","");
					String[] pkSplit = primaryKey.split(",");
					for(String pkColumn:pkSplit) {
						if(pkColumn!=null && !pkColumn.isEmpty())
						   pkColumList.add(pkColumn);
					}
					/*String[] pkString2 = null;
					if (primaryKey.endsWith(",")) {
						pkString2 = primaryKey.split(",");
						if (pkString2.length > 1) {
							primaryKey = pkString2[0] + "," + pkString2[1];
							pkColumList.add(pkString2[0].replaceAll(PRIFOREIGNEXP, ""));
							pkColumList.add(pkString2[1].replaceAll(PRIFOREIGNEXP, ""));
						}
						else {
							primaryKey = pkString2[0];
							pkColumList.add(pkString2[0].replaceAll(PRIFOREIGNEXP, ""));
						}
					}*/
					constraint.setColumns(pkColumList);
					constraintList.add(constraint);
				} else if (lineFromFile.contains(CONSTRAINT)) {
					if(lineFromFile.contains(FOREIGN_KEY)) {
						ForigenKeyConstraint fkConstraint = new ForigenKeyConstraint();
						String[] fieldString = lineFromFile.split(FOREIGN_KEY);
						fkConstraint.setConstraintName(fieldString[0].replace(CONSTRAINT, "").replaceAll("`", "").trim());
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
						fkConstraint.setReferenceColumn(test3[1].replaceAll(PRIFOREIGNEXP, ""));
						forigenKeysList.add(fkConstraint);
					}else if(lineFromFile.contains(UNIQUE_KEY)) {
						Constraint constraint = new Constraint();
						List<String> uniqueList =  new ArrayList<>();
						String[] uniqueSplit = lineFromFile.split(UNIQUE_KEY);
						constraint.setConstraintName("Unique");
						
						
					}else if(lineFromFile.contains(CHECK)) {
						CheckConstraint checkConstraint = new CheckConstraint();
						String[] chkConstraintSplit = lineFromFile.split(CHECK);
						checkConstraint.setConstraintName(chkConstraintSplit[0].replace(CONSTRAINT, "").replaceAll("`", "").trim());
						checkConstraint.setValue(chkConstraintSplit[1].replaceAll(CHECKEXP, "").trim());
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
					String lineFromFile1 = lineFromFile.trim().replaceAll(FIELDEXP, " ").trim();
					String[] splitString =  lineFromFile1.split(" ");
					coulmnField.setColumnName(splitString[0]);
					String[] lengthSplit = splitString[1].split("\\(");
					if(lengthSplit.length>1) {
					  coulmnField.setDataType(lengthSplit[0].replaceAll("[^\\,a-zA-Z0-9]+", ""));
					  coulmnField.setLength(lengthSplit[1].replaceAll("[^\\,a-zA-Z0-9]+", ""));
					}else
						coulmnField.setDataType(lengthSplit[0].replace(",", ""));
					if(splitString.length>2) {
						coulmnField.setDefaultValue(splitString[2]+" "+splitString[3].replace(",", ""));
						constraint.setConstraintType(splitString[2]+" "+splitString[3].replace(",", ""));
						pkColumList.add(splitString[0]);
						if(splitString.length>4) {
							coulmnField.setIncrementValue(splitString[4].replace(",", ""));
						}
					}
					constraint.setColumns(pkColumList);
					constraintList.add(constraint);
					fieldsList.add(coulmnField);
					String[] fieldString = lineFromFile1.split(" ");
					if (fieldString.length >= 2) {
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

  
  
  
}
