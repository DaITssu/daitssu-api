package com.example.test

import com.example.common.configuration.ReflectionConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = [ReflectionConfiguration.BASE_PACKAGE])
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}
