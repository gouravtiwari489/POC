package com.datagenerator.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.datagenerator.demo.domain.GenerateDataObject;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

@Component
public class TableStructureExtractor {

	private static final String ENGINE = "ENGINE";
	private static final String REFERENCES = "REFERENCES ";
	private static final String FOREIGN_KEY = "FOREIGN KEY ";
	private static final String PRIMARY_KEY = "PRIMARY KEY ";
	private static final String CREATE_TABLE = "CREATE TABLE ";
	
	@Autowired
	private CustomTokenConverter customTokenConverter;

	public LinkedHashMap<String, LinkedHashMap<String, String>> searchforTableName(File file)
			throws FileNotFoundException {
		LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = new LinkedHashMap<>();
		final Scanner scanner = new Scanner(file);
		String tableName = "", primaryKey = "";
		LinkedHashMap<String, String> fieldMap = null;
		int count = 0, fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains(CREATE_TABLE)) {
				fieldMap = new LinkedHashMap<>();
				tableName = "";
				fkCount = 1;
				String[] matchString = lineFromFile.split(CREATE_TABLE);
				tableName = matchString[1].split(" ")[0].replace("`", "");
				count = 1;
			} else if (lineFromFile.contains(PRIMARY_KEY)) {
				primaryKey = "";
				count = 0;
				String[] pkString = lineFromFile.split(PRIMARY_KEY);
				primaryKey = pkString[1].split(" ")[0].replace("`", "");
				if (primaryKey.length() > 1)
					fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 2));
			} else if (lineFromFile.contains(FOREIGN_KEY)) {
				String[] fieldString = lineFromFile.split(FOREIGN_KEY);
				String[] fieldString2 = fieldString[1].split(REFERENCES);
				System.out.println("fieldString for fk is::" + fieldString2[0] + "--" + fieldString2[1]);
				String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
				String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "")
						.replace("ONUPDATECASCADE", "");
				fieldMap.put("FK" + fkCount + "->" + test1, test2);
				count = 0;
				fkCount++;
			} else if (lineFromFile.contains(ENGINE)) {
				count = 0;
			} else if (count == 1) {
				String[] fieldString = lineFromFile.split(" ");
				if (fieldString.length > 2) {
					String field = fieldString[2].replace("`", "");
					String fieldType = fieldString[3].replace("`", "");
					fieldType = fieldType.replace(",", "");
					fieldMap.put(field, fieldType);
				}
			}
			if ((tableName != "" && null != tableName) && (fieldMap != null && !fieldMap.isEmpty()))
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		reOrderTableStructure(tableMap);
		return tableMap;
	}

	private void reOrderTableStructure(LinkedHashMap<String, LinkedHashMap<String, String>> tableMap) {
		LinkedHashMap<String, List<String>> fkListMap = createFKListMap(tableMap);
		LinkedHashMap<String, List<String>> sorted = sortMap(fkListMap);
		List<GenerateDataObject> list = transform(sorted);
		customTokenConverter.setAdditionalInfo("orderedFKList", list);
		System.out.println(customTokenConverter.getAdditionalInfo("orderedFKList"));
		//doPairing(sorted);
		//JSONObject json1 = new JSONObject(sorted);
	//	System.out.println(json1);

	}

	private LinkedHashMap<String, List<String>> doPairing(LinkedHashMap<String, List<String>> sorted) {
		LinkedHashMap<String, List<String>> paired = new LinkedHashMap<>();

		sorted.forEach((k, v) -> {

			paired.put(k, new ArrayList<String>());

			if (v.isEmpty()) {
				paired.put(k, new ArrayList<String>());
			} else {
				v.stream().forEach(index -> {
					List<String> val = new ArrayList<>();
					if (paired.get(index) != null)
						val = paired.get(index);
					val.add(k);
					paired.put(index, val);
				});
			}

		});

		return sortMap(paired);
	}

	private LinkedHashMap<String, List<String>> sortMap(LinkedHashMap<String, List<String>> inputMap) {
		return inputMap.entrySet().stream().sorted(comparingInt(e -> e.getValue().size()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

	}

	public List<GenerateDataObject> getFKMap(File file) throws FileNotFoundException {
		final Scanner scanner = new Scanner(file);
		LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = createTableMap(scanner);
		LinkedHashMap<String, List<String>> fkListMap = createFKListMap(tableMap);
		LinkedHashMap<String, List<String>> sorted = sortFKListMap(fkListMap);

		return transform(sorted);
	}

	public List<GenerateDataObject> transform(LinkedHashMap<String, List<String>> tableMap) {
		List<GenerateDataObject> tableList = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
			List<GenerateDataObject> tableList1 = new ArrayList<>();
			transformMaptoListData(tableList1, entry.getKey(), entry.getValue(), tableMap);
			tableList.addAll(tableList1);
		}
		return tableList;
	}

	private void transformMaptoListData(List<GenerateDataObject> tableList, String child, List<String> parent,
			LinkedHashMap<String, List<String>> tableMap) {
		boolean isFound = false;
		List<String> childTableName = null;
		List<String> parent1 = null;
		for (GenerateDataObject genDataObj : tableList) {
			if (parent.contains(genDataObj.getTableName())) {
				isFound = true;
				addChildToParent(genDataObj, child);
				childTableName = filterParentTable(child, tableMap);
				if (!childTableName.isEmpty()) {
					for (String child2 : childTableName) {
						parent1 = new ArrayList<String>();
						parent1.add(child);
						transformMaptoListData(tableList, child2, parent1, tableMap);
					}
				}
				break;
			} else if (genDataObj.getChildTableName() != null) {
				transformMaptoListData(genDataObj.getChildTableName(), child, parent, tableMap);
			}
		}
		if (!isFound) {
			if (parent.isEmpty()) {
				GenerateDataObject child1 = new GenerateDataObject();
				child1.setTableName(child);
				tableList.add(child1);
				childTableName = filterParentTable(child, tableMap);
				if (!childTableName.isEmpty()) {
					for (String child2 : childTableName) {
						parent1 = new ArrayList<String>();
						parent1.add(child);
						transformMaptoListData(tableList, child2, parent1, tableMap);
					}
				}

			}
		}

	}

	private List<String> filterParentTable(String child, Map<String, List<String>> tableMap) {
		List<String> childTableName = new ArrayList<String>();
		for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
			List<String> parentList = entry.getValue();
			if (parentList.contains(child)) {
				childTableName.add(entry.getKey());
			}
		}
		return childTableName;
	}

	private void addChildToParent(GenerateDataObject parent, String child) {
		if (!(parent.getChildTableName() != null && parent.getChildTableName().size() > 0)) {
			List<GenerateDataObject> childList = new LinkedList<GenerateDataObject>();
			parent.setChildTableName(childList);
		}
		GenerateDataObject child1 = new GenerateDataObject();
		child1.setTableName(child);
		parent.getChildTableName().add(child1);
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> createTableMap(final Scanner scanner) {
		LinkedHashMap<String, LinkedHashMap<String, String>> tableMap = new LinkedHashMap<>();
		String tableName = "";
		String primaryKey = "";
		LinkedHashMap<String, String> fieldMap = null;
		int count = 0;
		int fkCount = 1;
		while (scanner.hasNextLine()) {
			final String lineFromFile = scanner.nextLine();
			if (lineFromFile.contains(CREATE_TABLE)) {
				fieldMap = new LinkedHashMap<>();
				fkCount = 1;
				String[] matchString = lineFromFile.split(CREATE_TABLE);
				tableName = matchString[1].split(" ")[0].replace("`", "");
				count = 1;
			} else if (lineFromFile.contains(PRIMARY_KEY)) {
				count = 0;
				String[] pkString = lineFromFile.split(PRIMARY_KEY);
				primaryKey = pkString[1].split(" ")[0].replace("`", "");
				if (primaryKey.length() > 1)
					fieldMap.put("PK", primaryKey.substring(1, primaryKey.length() - 2));
			} else if (lineFromFile.contains(FOREIGN_KEY)) {
				String[] fieldString = lineFromFile.split(FOREIGN_KEY);
				String[] fieldString2 = fieldString[1].split(REFERENCES);
				System.out.println("fieldString for fk is::" + fieldString2[0] + "--" + fieldString2[1]);
				String test1 = fieldString2[0].replace("(", "").replace(")", "").replace("`", "").replace(" ", "");
				String test2 = fieldString2[1].replace(" ", "").replace("`", "").replace(",", "")
						.replace("ONUPDATECASCADE", "");
				fieldMap.put("FK" + fkCount + "->" + test1, test2);
				count = 0;
				fkCount++;
			} else if (lineFromFile.contains(ENGINE)) {
				count = 0;
			} else if (count == 1) {
				String[] fieldString = lineFromFile.split(" ");
				if (fieldString.length > 2) {
					String field = fieldString[2].replace("`", "");
					String fieldType = fieldString[3].replace("`", "");
					fieldMap.put(field, fieldType);
				}
			}
			if (tableName != "" || null != tableName && fieldMap != null)
				tableMap.put(tableName, fieldMap);
		}
		scanner.close();
		return tableMap;
	}

	private LinkedHashMap<String, List<String>> sortFKListMap(LinkedHashMap<String, List<String>> fkListMap) {
		return fkListMap.entrySet().stream().sorted(comparingInt(e -> e.getValue().size()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));
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
}
