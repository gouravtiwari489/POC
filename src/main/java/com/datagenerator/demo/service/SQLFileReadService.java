package com.datagenerator.demo.service;

import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.datagenerator.demo.exception.DependencyException;

public interface SQLFileReadService {

  List<LinkedHashMap<String, LinkedHashMap<String, String>>> readSQLfile(
      MultipartFile file, String domainType, boolean dependencyCheck) throws DependencyException, Exception;
}
