package com.minizin.travel.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class: SwaggerConfig Project: travel Package: com.minizin.travel.config
 * <p>
 * Description: SwaggerConfig
 *
 * @author dong-hoshin
 * @date 6/8/24 03:09 Copyright (c) 2024 miniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "travel APIDoc",
        description = "travel 관련 API"
    )
)
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
            .group("travel")
            .pathsToMatch("/**")
            .packagesToScan("com.minizin.travel")
            .build();
    }

}
