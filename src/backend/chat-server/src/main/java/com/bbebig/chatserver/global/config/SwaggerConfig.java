package com.bbebig.chatserver.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI api() {
		Info info = new Info()
				.title("BBeBig Service Server API")
				.version("1.0")
				.description("BBeBig Service Server API documentation");

		// 배포 서버 URL
		Server productionServer = new Server()
				.url("")
				.description("Production Server");

		// 로컬 서버 URL
		Server localServer = new Server()
				.url("http://localhost:8080")
				.description("Local Development Server");


		return new OpenAPI()
				.info(info)
				.servers(List.of(productionServer, localServer));
	}
}
