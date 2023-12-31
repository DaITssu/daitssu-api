package com.example.daitssuapi.domain.course.dto.response

import java.time.LocalDateTime

data class AssignmentResponse(
    val id: Long,
    val courseId: Long,
    val name: String,
    val dueAt: LocalDateTime? = null,
    val startAt: LocalDateTime? = null,
    val submitAt: LocalDateTime? = null,
    val detail: String? = null,
    val comments: String? = null
)
