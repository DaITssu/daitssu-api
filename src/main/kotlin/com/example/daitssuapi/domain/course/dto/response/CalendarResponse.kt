package com.example.daitssuapi.domain.course.dto.response

import com.example.daitssuapi.common.enums.CalendarType
import java.time.LocalDateTime

data class CalendarResponse(
    val id: Long,
    val type: CalendarType,
    val dueAt: LocalDateTime,
    val name: String,
    val isCompleted: Boolean
)
