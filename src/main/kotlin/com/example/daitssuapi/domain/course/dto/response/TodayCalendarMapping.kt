package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

interface TodayCalendarMapping {
    fun getCourse(): String
    fun getDueAt(): LocalDateTime
    fun getCount(): Int
}

