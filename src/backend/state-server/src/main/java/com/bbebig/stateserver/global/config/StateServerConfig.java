package com.bbebig.stateserver.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.bbebig.commonmodule.kafka.config")
public class StateServerConfig {
}
