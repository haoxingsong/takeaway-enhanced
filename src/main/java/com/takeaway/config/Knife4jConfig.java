package com.takeaway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 接口文档配置（基于 OpenAPI3）
 * 文档地址：http://localhost:8080/doc.html
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("苍穹外卖增强版接口文档")
                .description("Takeaway Enhanced API")
                .version("v1.0.0")
                .contact(new Contact().name("Takeaway Enhanced")));
    }
}
