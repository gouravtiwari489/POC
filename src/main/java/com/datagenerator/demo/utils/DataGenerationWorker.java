package com.datagenerator.demo.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.datagenerator.demo.serviceImpl.DataGenerationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {

	private String tableName;
	private HSSFWorkbook workbook;

	public DataGenerationWorker(String tableName, HSSFWorkbook workbook) {

		this.tableName = tableName;
		this.workbook = workbook;
	}

	@Override
	public void run() {

		try {
			generateData(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("data generation completed for " + tableName);
	}

	private void generateData(String tableName) throws Exception {

		Map<String, String> fieldMap = getMappedFields(tableName);
		log.info("fieldMap:::::::::::::::::::" + fieldMap + "");
		List<List<String>> excelData = GenerateSampleDataUtil.generateData(fieldMap);
		GenerateExcelUtil.createAndInsertDataIntoSheet(workbook, tableName, excelData);
	}

	private Map<String, String> getMappedFields(String tableName) {
		List<LinkedHashMap<String, LinkedHashMap<String, String>>> finalFieldMap = DataGenerationService.tablFieldMappingeMap;
		for (LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap : finalFieldMap) {
			if (linkedHashMap.get(tableName) != null) {
				return linkedHashMap.get(tableName);
			} else {
				continue;
			}
		}
		return null;
	}

}
