package com.example.domain.course.dto.request

import com.example.common.enums.CalendarType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "일정 추가 API Request Body")
class CalendarRequest (
    @Schema(description = "일정의 종류")
    val type: CalendarType,
    @Schema(description = "일정의 분류")
    val course: String,
    @Schema(description = "일정의 마감기한")
    val dueAt: String,
    @Schema(description = "일정의 제목")
    val name: String,
)