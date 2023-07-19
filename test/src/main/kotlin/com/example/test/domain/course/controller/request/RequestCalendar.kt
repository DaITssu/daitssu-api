package com.example.test.domain.course.controller.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Schema(name = "일정 추가 API Request Body")
class RequestCalendar (
    @Schema(description = "[필수] 일정의 종류입니다. ")
    val type: String,
    @Schema(description = "[필수] 일정의 분류입니다. ")
    val course: String,
    @Schema(description = "[필수] 일정의 마감기한입니다. ")
    val dueAt: String,
    @Schema(description = "[필수] 일정의 제목입니다. ")
    val name: String,
)