package com.datagenerator.demo.download.utils;

import java.util.List;

public enum CSVGenerationUtil implements GenerateDataInterface {

	INSTANCE;

	@Override
	public void generateData(String tableName, List<List<String>> excelData,
			String fileType) {
		ExcelGenerationUtil.INSTANCE.generateData(tableName, excelData,
				fileType);
	}

	@Override
	public void writeToFile(Object obj, String tableName, String fileType) {
		System.out.println();
	}

}
