package com.example.daitssuapi.domain.main.dto.response

import java.time.LocalDateTime

data class ArticleResponse(
    val id: Long,
    val topic: String,
    val title: String,
    val content: String,
    val writerNickName: String,
//    val images // image 데이터
    val updatedAt: LocalDateTime,
)