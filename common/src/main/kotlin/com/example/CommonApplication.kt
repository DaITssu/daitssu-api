package com.example

import com.example.common.configuration.ReflectionConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication(scanBasePackages = [ReflectionConfiguration.BASE_PACKAGE])
@EnableJpaAuditing
class CommonApplication

fun main(args: Array<String>) {
    runApplication<CommonApplication>(*args)
}
