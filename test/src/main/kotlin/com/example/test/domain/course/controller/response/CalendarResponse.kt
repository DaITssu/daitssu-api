package com.example.test.domain.course.controller.response

import com.example.test.domain.course.entity.CalendarCourseType

class CalendarResponse (
    
    val type: CalendarCourseType,
    val dueAt: String,
    val name: String,
)