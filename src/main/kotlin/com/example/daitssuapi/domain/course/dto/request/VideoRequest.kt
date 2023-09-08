package com.example.daitssuapi.domain.course.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "강의 등록 API Request Body입니다. ")
data class VideoRequest(
    @Schema(description = "강의 id")
    val courseId: Long,
    @Schema(description = "강의 이름")
    val name: String
)