package com.example.daitssuapi.domain.auth.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.auth.controller.request.RefreshRequest
import com.example.daitssuapi.domain.auth.controller.request.SignInRequest
import com.example.daitssuapi.domain.auth.controller.request.SignUpRequest
import com.example.daitssuapi.domain.auth.controller.response.AuthInfoResponse
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
    private val argumentResolver: ArgumentResolver
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
                name = signUpRequest.name,
                departmentId = signUpRequest.departmentId,
                studentId = signUpRequest.studentId,
                term = signUpRequest.term,
            )
        )
    }

    @Operation(
            summary = "유저 정보 조회",
            description = "회원가입을 위한 유저 학적 정보 조회 API입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
            ],
    )
    @PostMapping("/info")
    fun getUserInfo(
        @RequestBody
        signInRequest: SignInRequest,
    ): Response<AuthInfoResponse> {
        return Response(
            data = authService.getUserInfo(
                studentId = signInRequest.studentId,
                password = signInRequest.password,
            )
        )
    }

    @Operation(
        summary = "로그인",
        description = "로그인 API입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "401", description = "PASSWORD_INCORRECT"),
            ApiResponse(responseCode = "404", description = "USER_NOT_FOUND"),
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
                password = signInRequest.password,
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
    fun refresh(
        @RequestBody
        refreshRequest: RefreshRequest,
    ): Response<AuthResponse> {
        return Response(
            data = authService.refresh(
                refreshToken = refreshRequest.refreshToken,
            )
        )
    }

    @Operation(
        summary = "스마트캠퍼스에서 유저 정보 불러오기",
        description = "스마트캠퍼스에서 유저의 정보를 모두 불러옵니다. 작업이 오래걸릴 수 있습니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PostMapping("/refresh-info")
    fun refreshInfo(): Response<Nothing> {
        val userId = argumentResolver.resolveUserId()

        authService.refreshInfo(userId)

        return Response(code = 0, message = "OK", data = null)
    }
}
