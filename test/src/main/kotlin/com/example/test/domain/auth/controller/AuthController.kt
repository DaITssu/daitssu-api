package com.example.test.domain.auth.controller

import com.example.common.dto.UserDto
import com.example.test.domain.auth.controller.request.CreateUserRequest
import com.example.test.domain.auth.controller.request.LoginRequest
import com.example.test.domain.auth.controller.response.LoginResponse
import com.example.test.domain.auth.service.AuthService
import com.example.test.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 관련 API")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    @Operation(
        summary = "고객 회원가입",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
            ),
            ApiResponse(
                responseCode = "409",
                description = "해당하는 고객이 이미 존재합니다.",
                content = arrayOf(Content(schema = Schema(hidden = true))),
            ),
        ],

        )
    @PostMapping("/signUp")
    fun createUser(
        @RequestBody
        @Valid
        createUserRequest: CreateUserRequest,
    ): UserDto = userService.createUser(
        email = createUserRequest.email,
        password = createUserRequest.password,
        username = createUserRequest.username,
        role = createUserRequest.role,
    )

    @Operation(
        summary = "로그인",
        description = "로그인 API입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PostMapping("/login")
    fun login(
        @RequestBody
        loginRequest: LoginRequest,
    ): LoginResponse = authService.login(
        email = loginRequest.email,
        password = loginRequest.password,
    )
}