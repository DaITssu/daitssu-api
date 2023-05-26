package com.example.test.domain.auth.service

import com.example.test.domain.auth.component.TokenProvider
import com.example.test.domain.auth.controller.response.LoginResponse
import com.example.test.domain.user.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
) {
    fun login(
        email: String,
        password: String,
    ): LoginResponse {
        val token = getToken(
            email = email,
            password = password
        )

        val user = userService.getByEmail(email)

        return LoginResponse(
            email = user.email,
            username = user.username,
            role = user.userRole,
            accessToken = token,
        )
    }

    private fun getToken(
        email: String,
        password: String,
    ): String {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            email,
            password,
        )

        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return tokenProvider.createToken(authentication)
    }
}
