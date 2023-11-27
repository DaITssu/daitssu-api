package com.example.daitssuapi.domain.auth.controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "비밀번호 변경 요청 스키마입니다.")
data class ChangePasswordRequest(
    @Schema(description = "기존 비밀번호. 스마트캠퍼스의 기존 비밀번호입니다.")
    val previousPassword: String,
    @Schema(description = "변경할 비밀번호. 스마트캠퍼스와 동일한 비밀번호입니다.")
    val newPassword: String,
)
