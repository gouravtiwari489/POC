package datagenerator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DomainRepository {

	@Autowired
	private MongoOperations mongoOperations;
	
	public List<Domain> retrieveUserByRole(String role) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is("HRMS"));
		return mongoOperations.find(query, Domain.class);
	}
}
