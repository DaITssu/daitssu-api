package com.example.daitssuapi.domain.auth.controller

import com.example.daitssuapi.common.dto.Response
import com.example.daitssuapi.common.security.component.ArgumentResolver
import com.example.daitssuapi.domain.auth.controller.request.ChangePasswordRequest
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
                password = signInRequest.password,
            )
        )
    }

    @Operation(
        summary = "비밀번호 변경",
        description = "비밀번호 변경 API입니다. 이 API는 비밀번호 분실이 아닌 스마트캠퍼스 비밀번호의 변경 시 사용됩니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
        ],
    )
    @PatchMapping("/change-password")
    fun changePassword(
        @RequestBody
        changePasswordRequest: ChangePasswordRequest
    ): Response<AuthResponse> {
        val userId = argumentResolver.resolveUserId()

        return Response(
            data = authService.changePassword(
                userId = userId,
                previousPassword = changePasswordRequest.previousPassword,
                newPassword = changePasswordRequest.newPassword,
            )
        )
    }
}
