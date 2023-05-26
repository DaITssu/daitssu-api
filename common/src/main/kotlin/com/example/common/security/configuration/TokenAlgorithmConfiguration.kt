package com.example.common.security.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenAlgorithmConfiguration {
    val tokenAlgorithm: Algorithm
        @Bean
        get() {
            val secret = "template-secret-key"
            return Algorithm.HMAC256(secret)
        }

    val tokenVerifier: JWTVerifier
        @Bean
        get() = JWT
            .require(tokenAlgorithm)
            .withClaimPresence("ema")
            .withClaimPresence("rol")
            .build()
}
