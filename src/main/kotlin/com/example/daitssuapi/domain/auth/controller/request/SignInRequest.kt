package com.example.daitssuapi.domain.auth.controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청 스키마입니다.")
data class SignInRequest(
    @Schema(description = "학번")
    val studentId: String,
    @Schema(description = "비밀번호")
    val password: String,
)
