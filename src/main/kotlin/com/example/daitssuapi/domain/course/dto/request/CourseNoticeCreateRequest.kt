package com.example.daitssuapi.domain.course.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "공지 등록 API Request Body")
data class CourseNoticeCreateRequest(
    @Schema(description = "강의 id")
    val courseId: Long,
    @Schema(description = "공지 이름")
    val name: String,
    @Schema(description = "공지 등록일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val registeredAt: LocalDateTime,
    @Schema(description = "공지 내용")
    val content: String,
    @Schema(description = "공지 첨부 파일")
    val fileUrl: List<String>? = null
)
