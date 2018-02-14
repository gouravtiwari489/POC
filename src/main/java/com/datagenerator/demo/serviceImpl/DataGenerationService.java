package com.datagenerator.demo.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.utils.CustomTokenConverter;
import com.datagenerator.demo.utils.Worker;

@Service
public class DataGenerationService {

	public static List<LinkedHashMap<String, LinkedHashMap<String, String>>> tableMap = null;
	private static List<GenerateDataObject> tableList = null;

	@Autowired
	private CustomTokenConverter customTokenConverter;

	public void generateData() {
		tableMap = (List<LinkedHashMap<String, LinkedHashMap<String, String>>>) customTokenConverter
				.getAdditionalInfo("mappedTables");
		tableList = (List<GenerateDataObject>) customTokenConverter.getAdditionalInfo("orderedFKList");

		threadService(tableList);

	}

	public static void threadService(List<GenerateDataObject> tableList) {

		CountDownLatch countDownLatch = new CountDownLatch(tableList.size());
		try {
			List<Thread> workers = new ArrayList<>();
			for (GenerateDataObject generateDataObject : tableList) {
				Thread worker = new Thread(new Worker(generateDataObject, countDownLatch));
				workers.add(worker);
			}
			workers.forEach(Thread::start);
			countDownLatch.await();
			System.out.println("Count down latch released");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	
}
