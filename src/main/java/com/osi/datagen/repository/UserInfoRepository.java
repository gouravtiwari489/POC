package com.osi.datagen.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserInfoRepository {

  /* @Autowired private MongoOperations mongoOperations;

  public boolean isUserExists(String emailId) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where("_id").exists(true).orOperator(Criteria.where("emailId").is(emailId)));
    return mongoOperations.exists(query, User.class);
  }*/
}
