package com.datagenerator.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface SQLFileReadService {

	void readSQLfile(MultipartFile file) throws Exception;

}
