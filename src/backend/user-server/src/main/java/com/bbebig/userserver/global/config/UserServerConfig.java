package com.bbebig.userserver.global.config;

import com.bbebig.commonmodule.passport.config.WebMvcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
		"com.bbebig.commonmodule.kafka.config",
		"com.bbebig.commonmodule.redis.config"
})
@Import(WebMvcConfig.class)
public class UserServerConfig {
}
