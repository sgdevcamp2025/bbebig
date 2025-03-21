package com.bbebig.userserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("BBeBig User Server API")
                .version("1.0")
                .description("BBeBig User Server API documentation");

        SecurityScheme accessTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Access Token");

        // 배포 서버 URL
        Server productionServer = new Server()
                .url("")
                .description("Production Server");

        // 로컬 서버 URL
        Server localServer = new Server()
                .url("http://localhost:9010")
                .description("Local Development Server");


        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("Access Token", accessTokenScheme))
                .addSecurityItem(securityRequirement)
                .servers(List.of(productionServer, localServer));
    }
}
