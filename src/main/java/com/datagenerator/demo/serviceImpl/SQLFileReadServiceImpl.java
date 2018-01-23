package com.datagenerator.demo.serviceImpl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datagenerator.demo.domain.Domain;
import com.datagenerator.demo.repository.DomainRepository;
import com.datagenerator.demo.service.SQLFileReadService;

@Service
public class SQLFileReadServiceImpl implements SQLFileReadService{

	@Autowired
	DomainRepository  domainRepository;
	@Override
	public void readSQLfile() throws Exception {
		
		List<Domain>  domains = domainRepository.retrieveDomainByName("HRMS");
		if(domains.get(0).getTables().get(0) != null){
			Map<String, LinkedHashMap<String, String>> map = new LinkedHashMap<>();
			map = (Map<String, LinkedHashMap<String, String>>) domains.get(0).getTables().get(0);
			for (String key : map.keySet()) {
				System.out.println("key: " + key + " value: " + map.get(key));
			}
			
		}
		
		System.out.println(domains.get(0).getTables().get(0).toString());
	}

}
