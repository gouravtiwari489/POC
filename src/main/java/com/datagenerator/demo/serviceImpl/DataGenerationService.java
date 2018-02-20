package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
		Map<Integer, List<String>> tablesMap = (Map<Integer, List<String>>) customTokenConverter.getAdditionalInfo("orderedFKList");
		log.info("tablFieldMappingeMap values after getting from context", tablFieldMappingeMap);
		log.info("tablesMap values after getting from context", tablesMap);
		threadService(tablesMap);

	}

	public void threadService(Map<Integer, List<String>> tablesMap) {

		String filePath = null;
		POIFSFileSystem pfs = null;
		HSSFWorkbook workbook = null;
		try {
			Resource resource = new ClassPathResource("output");
			filePath = resource.getFile().getPath() + "\\ExcelSheet.xls";
			File f = new File(filePath);
			Files.deleteIfExists(Paths.get(f.toURI()));
			if (new File(filePath).createNewFile()) {
				workbook = new HSSFWorkbook();
			} else {
				pfs = new POIFSFileSystem(new FileInputStream(filePath));
				workbook = new HSSFWorkbook(pfs);
			}
			for (Map.Entry<Integer, List<String>> entry : tablesMap.entrySet()) {
				log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				List<String> tablesList = entry.getValue();
				ExecutorService executor = Executors.newFixedThreadPool(tablesList.size());
				for (String tableName : tablesList) {
					Runnable dataGenerationWorker = new DataGenerationWorker(tableName, workbook);
					executor.execute(dataGenerationWorker);
				}
				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.MINUTES);
				/*while (!executor.isTerminated()) {
				}*/
				log.info("Finished all threads");
				FileOutputStream outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
				outputStream.close();
			}
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
