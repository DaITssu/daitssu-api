package com.example.common.exception.base

import org.springframework.http.HttpStatusCode

data class ExceptionReasonDto(
    val traceId: String?,
    val status: HttpStatusCode,
    val reason: String,
    val message: String,
)
