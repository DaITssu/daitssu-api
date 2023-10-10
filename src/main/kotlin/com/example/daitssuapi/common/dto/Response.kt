package com.example.daitssuapi.common.dto

import java.time.LocalDateTime

data class Response<T>(
    val traceId: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val code: Int = 0,
    val message: String = "",
    val data: T?
)
