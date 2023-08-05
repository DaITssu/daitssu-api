package com.example.domain.main.dto.request

import com.example.common.enums.CalendarCourseType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "일정 추가 API Request Body")
class CalendarRequest (
    @Schema(description = "일정의 종류입니다. ")
    val type: CalendarCourseType,
    @Schema(description = "일정의 분류입니다. ")
    val course: String,
    @Schema(description = "일정의 마감기한입니다. ")
    val dueAt: String,
    @Schema(description = "일정의 제목입니다. ")
    val name: String,
)