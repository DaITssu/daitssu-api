package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

data class UserCourseResponse(
    val name: String,
    val term: Int,
    val updatedAt: LocalDateTime
)
