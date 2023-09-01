package com.example.daitssuapi.domain.auth.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.auth.controller.request.SignInRequest
import com.example.daitssuapi.domain.auth.controller.request.SignUpRequest
import com.example.daitssuapi.domain.auth.controller.response.AuthResponse
import com.example.daitssuapi.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 관련 API")
class AuthController(
    private val authService: AuthService,
    private val argumentResolver: ArgumentResolver,
) {
    @Operation(
        summary = "회원가입",
        description = "회원가입 API입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody
        signUpRequest: SignUpRequest,
    ): Response<AuthResponse> {
        return Response(
            data = authService.signUp(
                nickname = signUpRequest.nickname,
                password = signUpRequest.password,
                name = signUpRequest.name,
                studentId = signUpRequest.studentId,
                departmentId = signUpRequest.departmentId,
                term = signUpRequest.term,
            )
        )
    }

    @Operation(
        summary = "로그인",
        description = "로그인 API입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody
        signInRequest: SignInRequest,
    ): Response<AuthResponse> {
        return Response(
            data = authService.signIn(
                studentId = signInRequest.studentId,
            )
        )
    }

    @Operation(
        summary = "토큰 리프레쉬",
        description = "토큰 리프레쉬 API입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PostMapping("/refresh")
    fun signIn(
        @RequestBody
        refreshToken: String,
    ): Response<AuthResponse> {
        return Response(
            data = authService.refresh(
                refreshToken = refreshToken,
            )
        )
    }
}
