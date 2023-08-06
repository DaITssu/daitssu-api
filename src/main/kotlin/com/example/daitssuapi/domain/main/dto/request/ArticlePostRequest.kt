package com.example.domain.main.dto.request

data class ArticlePostRequest(
    val title: String,
    val content: String,
    val nickname: String
//    val studentId: Int,
//    val images, // image 데이터
)