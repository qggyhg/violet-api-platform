package com.violet.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 开启 Swagger2 的功能
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 告诉系统：去扫描这个包下面的所有 Controller
                .apis(RequestHandlerSelectors.basePackage("com.violet.api"))
                .paths(PathSelectors.any())
                .build();
    }

    // 配置文档的封面信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Violet的 API 开放平台")
                .description("提供各种第三方接口调用服务，支持JWT鉴权与调用额度管控。")
                .contact(new Contact("Violet", "https://github.com", "violet@example.com"))
                .version("1.0.0")
                .build();
    }
}