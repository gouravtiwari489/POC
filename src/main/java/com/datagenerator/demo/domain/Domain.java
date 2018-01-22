package datagenerator;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Document(collection = "domain")
public class Domain {
	
	@Id
	String domainId;
	List<Object> tables;

}
