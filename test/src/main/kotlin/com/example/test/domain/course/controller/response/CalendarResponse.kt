package com.example.test.domain.course.controller.response

import com.example.test.domain.course.entity.CalendarCourseType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class CalendarResponse (
    
    val type: CalendarCourseType,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val dueAt: LocalDateTime,
    val name: String,
)