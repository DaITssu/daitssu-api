package com.example.daitssuapi.domain.mypage.dto.response

import java.time.LocalDateTime

data class MyScrapResponse (
    val id: Long,
    val topic: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val commentSize: Int
)