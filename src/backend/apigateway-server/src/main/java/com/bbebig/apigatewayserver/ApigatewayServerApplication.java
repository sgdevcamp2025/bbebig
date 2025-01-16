package com.bbebig.apigatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayServerApplication.class, args);
	}

}
