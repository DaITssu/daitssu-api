package com.example.common.security.component

import com.auth0.jwt.interfaces.JWTVerifier
import jakarta.servlet.http.HttpServletRequest
import com.example.common.dto.TokenDto

interface JwtVerifier {
    val tokenVerifier: JWTVerifier

    fun verify(request: HttpServletRequest): TokenDto?
}
