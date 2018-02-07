package com.datagenerator.demo.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.datagenerator.demo.domain.GenerateDataObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class MapToListTransformerService {

	public Map<String, List<String>> tableMap;

	public List<GenerateDataObject> transform() {
		List<GenerateDataObject> tableList = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
			List<GenerateDataObject> tableList1 = new ArrayList<>();
			transformMaptoListData(tableList1, entry.getKey(), entry.getValue());
			tableList.addAll(tableList1);
		}
		return tableList;
	}

	private void transformMaptoListData(List<GenerateDataObject> tableList, String child, List<String> parent) {
		boolean isFound = false;
		List<String> childTableName = null;
		List<String> parent1 = null;
		for (GenerateDataObject genDataObj : tableList) {
			if (parent.contains(genDataObj.getTableName())) {
				isFound = true;
				addChildToParent(genDataObj, child);
				childTableName = filterParentTable(child);
				if (!childTableName.isEmpty()) {
					for (String child2 : childTableName) {
						parent1 = new ArrayList<String>();
						parent1.add(child);
						transformMaptoListData(tableList, child2, parent1);
					}
				}
				break;
			} else if (genDataObj.getChildTableName() != null) {
				transformMaptoListData(genDataObj.getChildTableName(), child, parent);
			}
		}
		if (!isFound) {
			if (parent.isEmpty()) {
				GenerateDataObject child1 = new GenerateDataObject();
				child1.setTableName(child);
				tableList.add(child1);
				childTableName = filterParentTable(child);
				if (!childTableName.isEmpty()) {
					for (String child2 : childTableName) {
						parent1 = new ArrayList<String>();
						parent1.add(child);
						transformMaptoListData(tableList, child2, parent1);
					}
				}

			}
		}

	}
	
	private List<String> filterParentTable(String child) {
		List<String> childTableName = new ArrayList<String>();
		for (Map.Entry<String, List<String>> entry : tableMap.entrySet()) {
			List<String> parentList = entry.getValue();
			if (parentList.contains(child)) {
				childTableName.add(entry.getKey());
			}
		}
		return childTableName;
	}
	private  void addChildToParent(GenerateDataObject parent, String child) {
		if (!(parent.getChildTableName() != null && parent.getChildTableName().size() > 0)) {
			List<GenerateDataObject> childList = new LinkedList<GenerateDataObject>();
			parent.setChildTableName(childList);
		}
		GenerateDataObject child1 = new GenerateDataObject();
		child1.setTableName(child);
		parent.getChildTableName().add(child1);
	}

}
