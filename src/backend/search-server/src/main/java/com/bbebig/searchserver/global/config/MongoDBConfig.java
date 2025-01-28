package com.bbebig.searchserver.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {

	@Value("${mongodb.host}")
	private String mongodbHost;

	@Value("${mongodb.port}")
	private int mongodbPort;

	@Value("${mongodb.database}")
	private String databaseName;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Bean
	@Override
	public MongoClient mongoClient() {
		String connectionString = String.format("mongodb://%s:%d", mongodbHost, mongodbPort);
		return MongoClients.create(connectionString);
	}
}
