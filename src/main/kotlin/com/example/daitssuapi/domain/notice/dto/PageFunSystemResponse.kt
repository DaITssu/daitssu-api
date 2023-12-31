package com.example.daitssuapi.domain.notice.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(name = "펀시스템 조회 API response body")
data class PageFunSystemResponse(
    val funSystems: List<FunSystemResponse>,
    val totalPages: Long,
) : Serializable