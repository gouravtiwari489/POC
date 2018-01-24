package com.datagenerator.demo.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.datagenerator.demo.domain.Domain;

public interface DomainRepository extends MongoRepository<Domain, String>{

	
	public List<Domain> getDomainByDomainId(String name) ;
}
