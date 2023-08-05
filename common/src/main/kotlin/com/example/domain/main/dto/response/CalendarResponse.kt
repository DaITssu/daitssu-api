package com.example.domain.main.dto.response

import com.example.common.enums.CalendarType
import java.time.LocalDateTime

class CalendarResponse (
    
    val type: CalendarType,
    val dueAt: LocalDateTime,
    val name: String,
)