package com.example.domain.main.dto.response

import com.example.common.enums.CalendarCourseType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class CalendarResponse (
    
    val type: CalendarCourseType,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val dueAt: LocalDateTime,
    val name: String,
)