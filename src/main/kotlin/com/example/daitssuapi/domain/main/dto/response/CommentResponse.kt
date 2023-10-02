package com.example.daitssuapi.domain.main.dto.response

import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val userId: Long,
    val content: String,
    val originalCommentId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
