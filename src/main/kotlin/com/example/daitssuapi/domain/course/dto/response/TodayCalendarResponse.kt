package com.example.daitssuapi.domain.course.dto.response

data class TodayCalendarResponse (
    val assignments: List<TodayCalendarData> = emptyList(),
    val videos: List<TodayCalendarData> = emptyList(),
)

