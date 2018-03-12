package com.datagenerator.das.repository;

import com.datagenerator.das.domain.Domain;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainRepository extends MongoRepository<Domain, String> {

  public List<Domain> getDomainByDomainId(String name);
}
