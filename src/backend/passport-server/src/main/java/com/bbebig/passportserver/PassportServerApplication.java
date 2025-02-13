package com.bbebig.passportserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {
		"com.bbebig.commonmodule.passport",
		"com.bbebig.passportserver"
})
public class PassportServerApplication {

	public static void main(String[] args) {
		initEnv();
		SpringApplication.run(PassportServerApplication.class, args);
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
