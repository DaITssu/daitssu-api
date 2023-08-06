package com.example.domain.course.dto.response

import java.time.LocalDateTime

class AssignmentResponse(
    val id: Long,
    val name: String,
    val dueAt: LocalDateTime,
    val startAt: LocalDateTime
)