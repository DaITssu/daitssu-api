package com.example.daitssuapi.domain.auth.controller.response

import com.example.daitssuapi.domain.auth.dto.AuthTokenDto
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(description = "로그인 응답 스키마입니다.")
data class AuthResponse(
    @Schema(description = "액세스 토큰")
    val accessToken: AuthTokenDto,
    @Schema(description = "리프레시 토큰")
    val refreshToken: AuthTokenDto,
) : Serializable
