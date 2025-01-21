package com.bbebig.serviceserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceServerApplication {

	public static void main(String[] args) {
		initEnv();
		SpringApplication.run(ServiceServerApplication.class, args);
	}

	/**
	 * 스트링부트 실행 전 시스템 property를 설정한다.
	 */
	static void initEnv() {
		Dotenv.configure()
				.directory("./src/main/resources/")
				.filename(".env")
				.load()
				.entries()
				.forEach(e -> {
					System.setProperty(e.getKey(), e.getValue());
				});
	}
}
