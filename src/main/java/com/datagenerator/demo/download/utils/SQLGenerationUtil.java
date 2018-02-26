package com.datagenerator.demo.download.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public enum SQLGenerationUtil implements GenerateDataInterface {

	INSTANCE;

	@Override
	public void generateData(String tableName, List<List<String>> excelData,
			String fileType) {

	}

	@Override
	public void writeToFile(Object obj, String tableName, String fileType)
			throws IOException, FileNotFoundException {
		// TODO Auto-generated method stub

	}
}
