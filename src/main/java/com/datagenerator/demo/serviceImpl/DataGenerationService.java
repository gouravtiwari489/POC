package com.datagenerator.demo.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datagenerator.demo.utils.CustomTokenConverter;
import com.datagenerator.demo.utils.DataGenerationWorker;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataGenerationService {

	public static List<LinkedHashMap<String, LinkedHashMap<String, String>>> tablFieldMappingeMap = null;
	private static Map<Integer, List<String>> tablesMap = null;

	@Autowired
	private CustomTokenConverter customTokenConverter;

	public void generateData() {
		
		tablFieldMappingeMap = (List<LinkedHashMap<String, LinkedHashMap<String, String>>>) customTokenConverter
				.getAdditionalInfo("mappedTables");
		tablesMap = (Map<Integer, List<String>>) customTokenConverter.getAdditionalInfo("orderedFKList");
		log.info("tablFieldMappingeMap values after getting from context",tablFieldMappingeMap);
		log.info("tablesMap values after getting from context",tablesMap);
		threadService(tablesMap);

	}

	public  void threadService(Map<Integer, List<String>> tablesMap) {

		for (Map.Entry<Integer, List<String>> entry : tablesMap.entrySet()) {
			log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			List<String> tablesList = entry.getValue();
			CountDownLatch countDownLatch = new CountDownLatch(tablesList.size());
			try {
				List<Thread> workers = new ArrayList<>();
				for (String tableName : tablesList) {
					Thread worker = new Thread(new DataGenerationWorker(tableName, countDownLatch));
					workers.add(worker);
				}
				workers.forEach(Thread::start);
				countDownLatch.await();
			} catch (InterruptedException e) {
				log.error("exception while generating excel",e);
			}
		}

	}

}
