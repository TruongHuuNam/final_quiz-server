package com.huunam.identity_service.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Quiz Service API")
                                                .version("1.0.3")
                                                .description("API documentation for the Quiz Service")
                                                .contact(new Contact()
                                                                .name("Truong Huu Nam")
                                                                .email("truonghuunam2002@gmail.com")
                                                                .url("http://localhost:8080/identity")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .name("bearerAuth")
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")))
                                .servers(List.of(
                                                new Server().url("https://final-quiz-server.onrender.com/identity")
                                                                .description("Production Server"),
                                                new Server().url("http://localhost:8080/identity")
                                                                .description("Local Development Server"),
                                                new Server().url("https://quiz-server-production-eddc.up.railway.app")
                                                                .description("Production Server")));

        }
}
