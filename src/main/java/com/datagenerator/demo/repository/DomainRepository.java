package com.datagenerator.demo.repository;

import com.datagenerator.demo.domain.Domain;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainRepository extends MongoRepository<Domain, String> {

  public List<Domain> getDomainByDomainId(String name);
}
