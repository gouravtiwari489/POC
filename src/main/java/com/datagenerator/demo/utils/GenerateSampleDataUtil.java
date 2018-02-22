package com.datagenerator.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.datagenerator.demo.serviceImpl.Dataset;

@Service
public class GenerateSampleDataUtil {

	public static List<List<String>> generateData(Map<String, String> fieldMap, int rowCount) {
		List<List<String>> records = new ArrayList<List<String>>();
		records.add(new ArrayList<String>(fieldMap.keySet()));
		for (int i = 1; i <= rowCount; i++) {
			List<String> row = new ArrayList<String>();
			for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
				String[] values = entry.getValue().split("<>");
				if (values[1].contains("varchar")) {
					row.add(Dataset.getRandomData(values[0]));
				} else if (values[1].contains("int")) {
					row.add(i + "");
				} else if (values[1].contains("blob")) {
					row.add(Dataset.getRandomData(values[0]));
				}  else if (values[1].contains("date")) {
					row.add(Dataset.getRandomDate().toString());
				}else if (values[1].contains("text")) {
					row.add(Dataset.getRandomData(values[0]));
				}
				else {
					row.add(i + new Random().nextInt(23456) * 0.2 + "");
				}
			}
			records.add(row);
		}

		return records;
	}
}
