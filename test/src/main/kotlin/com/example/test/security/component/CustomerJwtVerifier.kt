package com.example.test.security.component

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.common.dto.TokenDto
import com.example.common.dto.UserTokenDto
import com.example.common.enums.UserRole
import com.example.common.exception.AuthenticateFailedException
import com.example.common.exception.JwtTokenExpiredException
import com.example.common.security.component.JwtVerifier
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class CustomerJwtVerifier(
    private val tokenAlgorithm: Algorithm,
) : JwtVerifier {
    override val tokenVerifier: JWTVerifier = JWT
        .require(tokenAlgorithm)
        .withClaimPresence("userId")
        .withClaimPresence("userRole")
        .build()

    override fun verify(request: HttpServletRequest): TokenDto? {
        val bearerToken = request.getHeader("Authorization") ?: return null
        if (!bearerToken.startsWith("Bearer ")) return null

        val userTokenDto = when (val token = bearerToken.substring(7)) {
            "test",
            -> UserTokenDto(
                userId = 1,
                userRole = UserRole.ADMIN,
            )

            // endregion
            else -> verifyToken(token)
        }

        return userTokenDto
    }

    private fun verifyToken(token: String): UserTokenDto {
        return try {
            val verification = tokenVerifier.verify(token)
            return UserTokenDto(
                userId = verification.getClaim("userId").asLong(),
                userRole = UserRole.valueOf(verification.getClaim("userRole").asString()),
            )
        } catch (e: TokenExpiredException) {
            throw JwtTokenExpiredException()
        } catch (e: JWTVerificationException) {
            throw AuthenticateFailedException()
        }
    }
}
