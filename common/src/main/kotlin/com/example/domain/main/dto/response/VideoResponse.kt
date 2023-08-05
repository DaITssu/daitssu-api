package com.example.domain.main.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

class VideoResponse (
    val id: Long,
    val name: String,
    val dueAt: LocalDateTime,
    val startAt: LocalDateTime
)