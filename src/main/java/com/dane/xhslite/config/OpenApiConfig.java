package com.dane.xhslite.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI xhsLiteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XHS Lite Backend API")
                        .description("Portfolio backend for user, follow, and note search scenarios")
                        .version("v1")
                        .contact(new Contact().name("Danqing Zhu").email("danqingzhu1024@gmail.com")));
    }
}
