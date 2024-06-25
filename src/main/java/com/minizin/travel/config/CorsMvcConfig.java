package com.minizin.travel.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        log.debug("Configuring CORS mappings");
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie", "Authorization")
                .allowedOrigins("http://localhost:3000", "https://fe-two-blond.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600L);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.debug("Configuring resource handlers for Swagger UI");
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/");
        registry.addResourceHandler("/v3/api-docs/**")
            .addResourceLocations("classpath:/META-INF/resources/");
    }
}
