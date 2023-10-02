package com.example.daitssuapi.domain.main.dto.request

data class CommentWriteRequest(
    val userId: Long,
    val content: String,
    val originalCommentId: Long? = null
)
