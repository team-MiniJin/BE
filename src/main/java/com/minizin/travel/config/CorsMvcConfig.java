package com.minizin.travel.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        log.debug("Configuring CORS mappings");
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie", "Authorization")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("POST", "PUT", "GET", "DELETE", "PATCH", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600L);
    }
}
