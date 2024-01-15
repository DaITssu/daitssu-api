package com.example.daitssuapi.domain.course.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "과제 등록 API Request Body")
data class AssignmentUpdateRequest(
    @Schema(description = "과제 id")
    val id: Long,
    @Schema(description = "과제 마감일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val dueAt: LocalDateTime? = null,
    @Schema(description = "과제 시작일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val startAt: LocalDateTime? = null,
    @Schema(description = "과제 제출일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val submitAt: LocalDateTime? = null,
    @Schema(description = "과제 상세 내용")
    val detail: String? = null,
    @Schema(description = "과제 Comment")
    val comments: String? = null
)
