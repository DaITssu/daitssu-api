package com.example.test.domain.auth.component

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    private val tokenAlgorithm: Algorithm,
) {
    fun createToken(
        authentication: Authentication,
    ): String {
        val authority = authentication.authorities
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))

        return JWT.create()
            .withClaim("ema", authentication.name)
            .withClaim("rol", authority)
            .withExpiresAt(
                Date.from(
                    LocalDateTime
                        .now()
                        .plusMinutes(30)
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant(),
                ),
            )
            .sign(tokenAlgorithm)
    }
}
