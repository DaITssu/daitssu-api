package com.example.daitssuapi.common.security.component

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.daitssuapi.domain.auth.dto.AuthTokenDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenProvider(
    private val tokenAlgorithm: Algorithm,
    @Value("\${app.jwt.accessTokenValidMS}") private val accessTokenValidMS: Long = 0,
    @Value("\${app.jwt.refreshTokenValidMS}") private val refreshTokenValidMiS: Long = 0
) {
    private fun createToken(
        id: Long,
        tokenValidMilSecond: Long,
    ): AuthTokenDto {
        val expiredIn = Date().time + tokenValidMilSecond

        val token = JWT.create()
            .withClaim("userId", id)
            .withClaim("userRole", "STUDENT")   // TODO: 추후 개발 요소: UserRole
            .withExpiresAt(
                Date(expiredIn)
            )
            .sign(tokenAlgorithm)

        return AuthTokenDto(
            token = token,
            expiredIn = tokenValidMilSecond,
        )
    }

    fun createAccessToken(
        id: Long,
    ): AuthTokenDto {
        return createToken(id, accessTokenValidMS)
    }

    fun createRefreshToken(
        id: Long,
    ): AuthTokenDto {
        return createToken(id, refreshTokenValidMiS)
    }
}
