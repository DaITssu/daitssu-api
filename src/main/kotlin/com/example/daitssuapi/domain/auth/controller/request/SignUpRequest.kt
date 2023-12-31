package com.example.daitssuapi.domain.auth.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(description = "회원가입 요청 스키마입니다.")
data class SignUpRequest(
    @Schema(description = "닉네임")
    val nickname: String,
    @Schema(description = "이름")
    val name: String,
    @Schema(description = "학부명입니다.")
    val departmentName: String,
    @Schema(description = "학번")
    val studentId: String,
    @Schema(description = "학기(ex.3학년 1학기 = 5)")
    val term: Int,
) : Serializable
