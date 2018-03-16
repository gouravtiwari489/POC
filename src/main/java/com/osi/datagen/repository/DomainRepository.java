package com.osi.datagen.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.osi.datagen.domain.Domain;

public interface DomainRepository extends MongoRepository<Domain, String> {

  public List<Domain> getDomainByDomainId(String name);
}
