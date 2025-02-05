package com.bbebig.searchserver.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoDBConfig extends AbstractMongoClientConfiguration {

	private String uri;

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	protected String getDatabaseName() {
		return uri.substring(uri.lastIndexOf("/") + 1); // DB 이름 추출
	}

	@Bean
	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(uri);
	}
}
