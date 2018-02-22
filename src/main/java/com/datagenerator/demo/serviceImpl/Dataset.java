package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Dataset {
	public static Map<String, List<String>> map = new HashMap<>();

	static {
		try{
			Resource resource = new ClassPathResource("datasets");
			resource.getFile().getPath();
			File [] files = new File(resource.getFile().getPath()).listFiles();
			
			for (File file : files) {
				List<String> lines = FileUtils.readLines(file, "utf-8");
				map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String getRandomData(String ColumnName) {
		List<String> data = map.get(ColumnName.toLowerCase());
		if(data!=null){
		return data.get(new Random().nextInt(data.size()));
		}else{
			return ColumnName.toLowerCase();
		}
	}

	public static Date  getRandomDate() {
		
		long  beginTime = Timestamp.valueOf("1900-01-01 00:00:00").getTime();
		long  endTime = System.currentTimeMillis();
		    long diff = endTime - beginTime + 1;
		    return new Date(beginTime + (long) (Math.random() * diff));
	}
}
