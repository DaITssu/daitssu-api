package com.example.daitssuapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
class DaitssuApiApplication

fun main(args: Array<String>) {
    runApplication<DaitssuApiApplication>(*args)
}
