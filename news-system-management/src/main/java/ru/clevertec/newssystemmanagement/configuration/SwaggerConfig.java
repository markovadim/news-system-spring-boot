package ru.clevertec.newssystemmanagement.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("News System")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("News System Application API")
                        .version("v1")
                        .description("Spring boot application for news system management")
                        .contact(new Contact().name("Markov Vadim")
                                .email("markovadim@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                        .description("Dev service")));
    }
}
