package com.example.domain.main.dto.response

import com.example.domain.main.model.entity.User
import java.time.LocalDateTime

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val writer: User,
//    val images // image 데이터
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)