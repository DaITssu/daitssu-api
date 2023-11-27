package com.example.daitssuapi.common.security.component

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.daitssuapi.common.dto.TokenDto
import com.example.daitssuapi.common.enums.ErrorCode
import com.example.daitssuapi.common.exception.DefaultException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class JwtVerifier(
    private val tokenAlgorithm: Algorithm,
    @Value("\${spring.profiles.active}") private val profile: String
) {
    private val alwaysAllowProfiles = listOf("test")

    val tokenVerifier: JWTVerifier = JWT
        .require(tokenAlgorithm)
        .withClaimPresence("userId")
        .withClaimPresence("userRole")
        .build()

    fun verify(request: HttpServletRequest): TokenDto? {
        if (profile in alwaysAllowProfiles) {
            return TokenDto(userId = 2, userRole = "STUDENT")
        }

        val bearerToken = request.getHeader("Authorization") ?: return null
        if (!bearerToken.startsWith("Bearer ")) {
            return null
        }

        return verifyToken(bearerToken)
    }

    fun verifyToken(bearerToken: String): TokenDto {
        // TODO: 실제 토큰을 넣어도 무시해버림 수정 필요
        if (profile in alwaysAllowProfiles) {
            return TokenDto(userId = 2, userRole = "STUDENT")
        }

        try {
            val token = bearerToken.substring(7)
            val verifiedJWT = tokenVerifier.verify(token)

            return TokenDto(
                userId = verifiedJWT.getClaim("userId").asLong(),
                userRole = verifiedJWT.getClaim("userRole").asString(),
            )
        } catch (e: TokenExpiredException) {
            throw DefaultException(ErrorCode.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED)
        } catch (e: JWTVerificationException) {
            throw DefaultException(ErrorCode.TOKEN_INVALID, HttpStatus.UNAUTHORIZED)
        }
    }
}
