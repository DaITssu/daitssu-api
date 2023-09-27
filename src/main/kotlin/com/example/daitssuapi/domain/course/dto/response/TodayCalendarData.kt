package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

data class TodayCalendarData (
    val course: String,
    val dueAt: LocalDateTime,
    val count: Int
)
