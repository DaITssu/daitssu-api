package com.example.daitssuapi.common.security.configuration

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenAlgorithmConfiguration(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
) {
    @Bean
    fun tokenAlgorithm(): Algorithm {
        val secret = jwtSecret
        return Algorithm.HMAC256(secret)
    }
}
