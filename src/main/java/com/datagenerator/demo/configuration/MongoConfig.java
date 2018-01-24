/*package com.datagenerator.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.beans.factory.annotation.Value;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;



@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Value("${data.mongodb.host}")
	private String mongoHost;

	@Value("${data.mongodb.port}")
	private String mongoPort;
	
	@Value("${data.mongodb.database}")
	private String mongoDB;
	
	@Override
	protected String getDatabaseName() {
		return mongoDB;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(mongoHost);
	}

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(), mongoDB);
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;

	}
}*/