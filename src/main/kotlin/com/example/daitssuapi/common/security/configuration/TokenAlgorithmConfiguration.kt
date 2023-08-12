package com.example.daitssuapi.common.security.configuration

import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenAlgorithmConfiguration() {
    @Bean
    fun tokenAlgorithm(): Algorithm {
        val secret = "daitssu"
        return Algorithm.HMAC256(secret)
    }
}
