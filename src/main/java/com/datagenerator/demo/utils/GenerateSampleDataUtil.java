package com.datagenerator.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.datagenerator.demo.serviceImpl.Dataset;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GenerateSampleDataUtil {
	
	public static List<List<String>> generateData(Map<String, String> fieldMap) {
		List<List<String>> records = new ArrayList<List<String>>();
		records.add(new ArrayList<String>(fieldMap.keySet()));
		for (int i = 0; i < 1000; i++) {
			List<String> row = new ArrayList<String>();
			for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
				if (entry.getValue().contains("int")) {
					row.add(i + "");
				} else if (entry.getValue().contains("decimal")) {
					row.add(i + new Random().nextInt(23456) * 0.2 + "");
				} else {
					row.add(Dataset.getRandomData(entry.getKey()));
				}
			}
			log.info("row "+row);
			records.add(row);
		}
		return records;
	}

}
