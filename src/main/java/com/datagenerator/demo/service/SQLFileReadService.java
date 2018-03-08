package com.datagenerator.demo.service;

import com.datagenerator.demo.exception.DependencyException;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SQLFileReadService {

  List<LinkedHashMap<String, LinkedHashMap<String, String>>> readSQLfile(
      MultipartFile file, String domainType, boolean dependencyCheck)
      throws DependencyException, Exception;
}
