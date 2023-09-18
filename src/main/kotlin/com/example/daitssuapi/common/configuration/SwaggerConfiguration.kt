package com.example.daitssuapi.common.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
@OpenAPIDefinition
class SwaggerConfiguration {
    private val securityScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .`in`(SecurityScheme.In.HEADER)
        .name("Authorization")
    private val securityRequirementName = "bearerAuth"

    init {
        SpringDocUtils
            .getConfig()
            .replaceWithSchema(
                Color::class.java,
                Schema<String>()
                    .type("string")
                    .format("color")
                    .example("#FFFFFFFF"),
            )
            .replaceWithSchema(
                LocalDateTime::class.java,
                Schema<LocalDateTime>()
                    .type("string")
                    .format("date-time")
                    .example(
                        LocalDateTime
                            .now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    ),
            )
            .replaceWithSchema(
                LocalDate::class.java,
                Schema<LocalDate>()
                    .type("string")
                    .format("date")
                    .example(
                        LocalDate
                            .now()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE),
                    ),
            )
            .replaceWithSchema(
                LocalTime::class.java,
                Schema<LocalTime>()
                    .type("string")
                    .format("time")
                    .example(
                        LocalTime
                            .now()
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    ),
            )
    }

    @Bean
    fun openApi(): OpenAPI {

        return OpenAPI()
            .servers(listOf(Server().apply { url = "/" }))
            .security(
                listOf(
                    SecurityRequirement()
                        .addList(securityRequirementName),
                ),
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        securityRequirementName,
                        securityScheme,
                    ),
            )
            .info(
                Info()
                    .title("Spring Boot API")
                    .description(description)
                    .version("0.0.1"),
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Spring Boot API"),
            )
    }

    private val apiRootUrl = "http://localhost:8080"
    private val description = """
        <h3>요청 헤더</h3>
        
        개발 환경은 토큰 없어도 됩니다.
        
        운영 환경에 필요한 요청 헤더 2개는 다음과 같습니다. <br />
        이때 "{토큰}" 대신 발급된 토큰을 넣어주세요. <br />
        
        * "Authorization: Bearer {토큰}"
        * "Content-Type: application/json"
        
        """.trimIndent()
}
