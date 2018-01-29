package com.datagenerator.demo.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.utils.TableStructureExtractor;

@Service
public class UploadService {

	@Autowired
	private TableStructureExtractor tableStructureExtractor;

	public void uploadFile(MultipartFile multipartFile) throws IOException {
		File convFile = new File(multipartFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multipartFile.getBytes());
		tableStructureExtractor.searchforTableName(convFile);
		fos.close();

	}

}
