package com.example.daitssuapi.domain.auth.controller.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "리프레시를 위한 토큰입니다.")
data class RefreshRequest(
    @Schema(description = "리프레쉬 토큰")
    val refreshToken: String,
)