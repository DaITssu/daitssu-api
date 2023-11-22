package com.example.daitssuapi.common.security.component

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.daitssuapi.common.dto.TokenDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtVerifier(
    private val tokenAlgorithm: Algorithm,
    @Value("\${spring.profiles.active}") private val profile: String
) {
    private val alwaysAllowProfiles = listOf("dev", "local", "test")

    val tokenVerifier: JWTVerifier = JWT
        .require(tokenAlgorithm)
        .withClaimPresence("userId")
        .withClaimPresence("userRole")
        .build()

    fun verify(request: HttpServletRequest): TokenDto? {
        if (profile in alwaysAllowProfiles) {
            return TokenDto(userId = 0, userRole = "STUDENT")
        }

        val bearerToken = request.getHeader("Authorization") ?: return null
        if (!bearerToken.startsWith("Bearer ")) {
            return null
        }

        return verifyToken(bearerToken)
    }

    fun verifyToken(bearerToken: String): TokenDto {
        try {
            val token = bearerToken.substring(7)
            val verifiedJWT = tokenVerifier.verify(token)

            return TokenDto(
                userId = verifiedJWT.getClaim("userId").asLong(),
                userRole = verifiedJWT.getClaim("userRole").asString(),
            )
        } catch (e: TokenExpiredException) {
            throw RuntimeException("토큰이 만료되었습니다.") // JwtTokenExpiredException()
        } catch (e: JWTVerificationException) {
            throw RuntimeException("인증 오류입니다.") // AuthenticateFailedException()
        }
    }
}
