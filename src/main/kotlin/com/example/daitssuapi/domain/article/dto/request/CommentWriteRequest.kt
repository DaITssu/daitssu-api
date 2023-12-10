package com.example.daitssuapi.domain.article.dto.request

data class CommentWriteRequest(
    val content: String,
    val originalCommentId: Long? = null
)
