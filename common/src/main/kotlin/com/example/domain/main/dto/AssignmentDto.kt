package com.example.domain.main.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class AssignmentDto (
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val dueAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val startAt: LocalDateTime,
    val name: String,
    val id: Long?,
)