package com.example.common

import com.example.common.configuration.ReflectionConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = [ReflectionConfiguration.BASE_PACKAGE])
class CommonApplication

fun main(args: Array<String>) {
    runApplication<CommonApplication>(*args)
}
