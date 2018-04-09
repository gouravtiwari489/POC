package com.osi.datagen.repository;

import com.osi.datagen.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoRepository {

  @Autowired private MongoOperations mongoOperations;

  public boolean isUserExists(String emailId) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where("_id").exists(true).orOperator(Criteria.where("emailId").is(emailId)));
    return mongoOperations.exists(query, User.class);
  }
}
