package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.datagenerator.demo.utils.CustomTokenConverter;
import com.datagenerator.demo.utils.DataGenerationWorker;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataGenerationService {

	public static List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap = null;

	@Autowired
	private CustomTokenConverter customTokenConverter;

	@SuppressWarnings("unchecked")
	public void generateData() {

		tablFieldMappingeMap = (List<LinkedHashMap<String, LinkedHashMap<String, String>>>) customTokenConverter
				.getAdditionalInfo("mappedTables");
		Map<Integer, List<String>> tablesMap = (Map<Integer, List<String>>) customTokenConverter
				.getAdditionalInfo("orderedFKList");
		log.info("tablFieldMappingeMap values after getting from context", tablFieldMappingeMap);
		log.info("tablesMap values after getting from context", tablesMap);
		threadService(tablesMap);

	}

	public void threadService(Map<Integer, List<String>> tablesMap) {
		String filePath = null;
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			for (Map.Entry<Integer, List<String>> entry : tablesMap.entrySet()) {
				log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				List<String> tablesList = entry.getValue();
				ExecutorService executor = Executors.newFixedThreadPool(tablesList.size());
				for (String tableName : tablesList) {
					Runnable dataGenerationWorker = new DataGenerationWorker(tableName, workbook);
					executor.execute(dataGenerationWorker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
			}
			Resource resource = new ClassPathResource("output");
			filePath = resource.getFile().getPath() + "\\ExcelSheet.xlsx";
			OutputStream excelFileToCreate = new FileOutputStream(new File(filePath));
			workbook.write(excelFileToCreate);
			excelFileToCreate.close();
		} catch (FileNotFoundException ex) {
			log.error("Error while creating excel", ex);
		} catch (IOException ex) {
			log.error("Error while creating excel", ex);
		} catch (Exception ex) {
			log.error("Error wrting to file", ex);
			ex.printStackTrace();
		}
	}
}
