package com.example.daitssuapi.domain.course.dto.response

data class TodayCalendarResponse (
    val assignments: List<TodayCalendarDataDto> = emptyList(),
    val videos: List<TodayCalendarDataDto> = emptyList(),
)

