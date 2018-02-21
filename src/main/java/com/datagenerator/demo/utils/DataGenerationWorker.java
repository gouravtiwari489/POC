package com.datagenerator.demo.utils;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataGenerationWorker implements Runnable {

	private String tableName;
	private XSSFWorkbook workbook;
	private LinkedHashMap<String, String> fieldMap;
	private int rowCount;
	private String fileType;

	public DataGenerationWorker(String tableName, XSSFWorkbook workbook, LinkedHashMap<String, String> fieldMap, int rowCount, String fileType) {

		this.tableName = tableName;
		this.workbook = workbook;
		this.fieldMap=fieldMap;
		this.rowCount=rowCount;
		this.fileType=fileType;
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

	private void generateData(String tableName) {
		try {

			List<List<String>> excelData = GenerateSampleDataUtil.generateData(fieldMap,rowCount);
			GenerateExcelUtil.createAndInsertDataIntoSheet(workbook, tableName, excelData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
