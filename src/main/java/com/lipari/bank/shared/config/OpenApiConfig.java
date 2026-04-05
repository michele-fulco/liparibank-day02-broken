package com.lipari.bank.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lipariBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LipariBank Core Backend API")
                        .description("API REST per la gestione di conti correnti — LipariBank S.p.A.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Lipari Consulting — Backend Team")
                                .email("backend@lipariconsulting.it"))
                        .license(new License()
                                .name("Uso Interno — Confidenziale")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente Development")
                ));
    }
}
