package com.example.daitssuapi.domain.course.dto.request

import io.swagger.v3.oas.annotations.media.Schema


@Schema(name = "과제 등록 API Request Body")
class AssignmentRequest(
    @Schema(description = "강의 id")
    val courseId: Long,
    @Schema(description = "과제 이름")
    val name: String,
)