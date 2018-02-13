package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
		return data.get(new Random().nextInt(100));
		}else{
			return ColumnName.toLowerCase();
		}

	}
	
	
	

}
