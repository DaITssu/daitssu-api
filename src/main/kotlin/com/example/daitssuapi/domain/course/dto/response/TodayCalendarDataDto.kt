package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

data class TodayCalendarDataDto (
    val course: String,
    val dueAt: LocalDateTime,
    val count: Int
)
