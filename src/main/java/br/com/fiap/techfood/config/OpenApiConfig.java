package br.com.fiap.techfood.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI techFood() {
        return new OpenAPI()
                .info(
                        new Info().title("TechFood API")
                                .description("Projeto desenvolvido durante primeira fase do tech challenge FIAP")
                                .version("v0.0.1")
                                .license(new License().name("Apache 2.0").url("https://github.com/lucaswalim"))
                );
    }
}
