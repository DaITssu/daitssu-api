package com.example.daitssuapi.domain.auth.controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청 스키마입니다.")
data class SignInRequest(
    @Schema(description = "학번")
    // TODO: 로그인 id는 학번으로 통일하기
    val studentId: String,
    @Schema(description = "비밀번호. 스마트캠퍼스와 동일한 비밀번호입니다.")
    val password: String,
)
