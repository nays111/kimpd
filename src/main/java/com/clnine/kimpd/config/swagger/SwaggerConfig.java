
package com.clnine.kimpd.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    private final static String accessToken = "key";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(accessToken, new SecurityScheme().type(SecurityScheme.Type.APIKEY).name("x-access-token").in(SecurityScheme.In.HEADER)))
                .addSecurityItem(new SecurityRequirement().addList(accessToken))
                .info(new Info()
                        .title("Kimpd API Server")
                        .version("v0.0.1"));
    }

}