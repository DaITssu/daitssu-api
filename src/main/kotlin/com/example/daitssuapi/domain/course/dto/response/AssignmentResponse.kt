package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

data class AssignmentResponse(
    val id: Long,
    val name: String,
    val dueAt: LocalDateTime,
    val startAt: LocalDateTime
)