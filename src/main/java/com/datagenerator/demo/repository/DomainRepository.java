package com.datagenerator.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datagenerator.demo.domain.Domain;

@Repository
public class DomainRepository {

	@Autowired
	private MongoOperations mongoOperations;
	
	public List<Domain> retrieveDomainByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(name));
		return mongoOperations.find(query, Domain.class);
	}
}
