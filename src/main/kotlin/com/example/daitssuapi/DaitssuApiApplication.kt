package com.example.daitssuapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class DaitssuApiApplication

fun main(args: Array<String>) {
    runApplication<DaitssuApiApplication>(*args)
}
