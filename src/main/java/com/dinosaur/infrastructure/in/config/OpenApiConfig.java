package com.dinosaur.infrastructure.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dinosaurOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dinosaur API")
                        .description(
                                "API REST del microservicio Dinosaur: alta, consulta, modificación y baja de registros de dinosaurios.")
                        .version("1.0"));
    }
}
