package com.bbebig.chatserver;

import com.bbebig.commonmodule.passport.config.WebMvcConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {
		"com.bbebig.chatserver",
		"com.bbebig.commonmodule"
})
@Import(WebMvcConfig.class)
public class ChatServerApplication {

	public static void main(String[] args) {
		initEnv();
		SpringApplication.run(ChatServerApplication.class, args);
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
