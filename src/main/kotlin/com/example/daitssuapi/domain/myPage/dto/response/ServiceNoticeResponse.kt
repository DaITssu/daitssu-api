package com.example.daitssuapi.domain.main.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "서비스 공지사항 조회 Response")
data class ServiceNoticeResponse (
    val title : String,
    val content : String,
    val createdAt : LocalDateTime
)