package com.example.daitssuapi.domain.course.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "공지 수정 API Request Body")
data class CourseNoticeUpdateRequest(
    @Schema(description = "공지 내용")
    val content: String? = null,
    @Schema(description = "공지 첨부 파일")
    val fileUrl: List<String>? = null
)
