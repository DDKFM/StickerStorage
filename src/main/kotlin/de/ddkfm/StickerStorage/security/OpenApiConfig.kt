package de.ddkfm.StickerStorage.security

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customApiConfig(): OpenAPI {

        return OpenAPI()
                .components(
                        Components()
                        .addSecuritySchemes("login",
                                SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .name("login"))
                        .addSecuritySchemes("api",
                                SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .name("api")))
                .info(Info()
                        .title("Sticker Storage API")
                        .description("a simple REST API for accessing your personal sticker collection")
                        .license(License()
                            .name("MIT License")
                            .url("https://github.com/DDKFM/StickerStorage")))
                .addSecurityItem(SecurityRequirement().addList("api"))
                .addSecurityItem(SecurityRequirement().addList("login"))
    }
}