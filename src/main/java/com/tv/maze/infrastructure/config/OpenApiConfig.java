package com.tv.maze.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	
	@Bean OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                    .title("TV Shows Management Microservice API")
                    .version("1.0.0")
                    .description("Microservicio desarrollado en Java 21 con Arquitectura Hexagonal y MongoDB Atlas para consulta de TV Shows en TVMaze y gestión de comentarios/ratings.")
                    .contact(new Contact()
                            .name("Soporte Técnico")
                            .email("lic.dario.resendiz@gmail.com")));
    }
}