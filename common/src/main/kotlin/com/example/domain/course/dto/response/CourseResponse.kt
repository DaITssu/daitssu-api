package com.example.domain.course.dto.response

import java.time.LocalDateTime

data class CourseResponse(
    val name: String,
    val term: Int,
    val updatedAt: LocalDateTime
)
