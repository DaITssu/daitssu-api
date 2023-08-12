package com.example.daitssuapi.domain.course.dto.response

import com.example.daitssuapi.common.enums.CalendarType
import java.time.LocalDateTime

class CalendarResponse(
    val type: CalendarType,
    val dueAt: LocalDateTime,
    val name: String,
)
