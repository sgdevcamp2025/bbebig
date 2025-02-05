package com.bbebig.serviceserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("BBeBig Service Server API")
                .version("1.0")
                .description("BBeBig Service Server API documentation");

        SecurityScheme passportScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-Passport");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("X-Passport");

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
                .components(new Components().addSecuritySchemes("X-Passport", passportScheme))
                .addSecurityItem(securityRequirement)
                .servers(List.of(productionServer, localServer));
    }
}
