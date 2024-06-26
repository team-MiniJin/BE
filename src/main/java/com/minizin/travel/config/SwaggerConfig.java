package com.minizin.travel.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class: SwaggerConfig Project: travel Package: com.minizin.travel.config
 * <p>
 * Description: SwaggerConfig
 *
 * @author dong-hoshin
 * @date 6/25/24 21:54 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
            .info(new Info()
                .title("travel API Document")
                .version("1.0")
                .description("travel Service Backend API Document"))
            .servers(List.of(
                new Server().url("http://lyckabc.synology.me:20280"),
                new Server().url("https://lyckabc.synology.me:23080")
            ));
    }

}
