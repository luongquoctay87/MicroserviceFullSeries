package vn.tayjava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.checkerframework.checker.units.qual.C;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Configuration
@Profile({"dev", "test"})
public class SpringFoxConfig {
    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
        return org.springdoc.core.models.GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/account-service
                .packagesToScan("vn.tayjava.controller")
                .build();
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl) {
        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .info(new Info().title(title)
                        .description("API documents for Account Service")
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
