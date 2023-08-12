package com.example.daitssuapi.domain.main.dto.request

import com.example.daitssuapi.domain.main.enums.Topic

data class ArticlePostRequest(
    val topic: Topic,
    val title: String,
    val content: String,
    val nickname: String? = null
//    val studentId: Int,
//    val images, // image 데이터
)