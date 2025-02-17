package com.bbebig.searchserver;

import com.bbebig.commonmodule.passport.config.WebMvcConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
		"com.bbebig.searchserver",
		"com.bbebig.commonmodule"
})
@Import(WebMvcConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SearchServerApplication {

	public static void main(String[] args) {
		initEnv();
		SpringApplication.run(SearchServerApplication.class, args);
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
